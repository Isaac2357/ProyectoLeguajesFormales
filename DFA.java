import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DFA{
    
    private ArrayList<State> states = null;
    private ArrayList<Transition> transitions = null;
    private State initialState= null;
    private State finalState = null;
    private List<Integer> finalStates = null;
    public static int IDCOUNTER = 0;
    private List<Character> alphabet = null;
    public DFA(){
        this.states = new ArrayList<State>();
        this.transitions = new ArrayList<Transition>();
        this.finalStates = new ArrayList<Integer>();
        this.alphabet = new ArrayList<Character>();
    }

    public List<Character> getAlphabet(){
        return this.alphabet;
    }

    public void addState(State state){
    	if(!this.states.contains(state)) {
    		this.states.add(state);
    	}
    }
    
    public void addStateAndFinal(State state) {
    	if(!this.states.contains(state)) {
    		this.states.add(state);
    	}
    	this.finalStates.add(state.getId());
    }

    public void addStates(State... states){
        for(State state : states){
            this.states.add(state);
        }   
    }

    public List<Transition> removeAllTransitions(){
    	ArrayList<Transition> tmp = (ArrayList<Transition>) this.transitions.clone();
    	this.transitions.clear();
    	return tmp;
    }
    
    public void addTransition(Transition transition){
        if(transition.getCharacter() != '&' 
           && !this.alphabet.contains(transition.getCharacter())){
            this.alphabet.add(transition.getCharacter());
        }
        this.transitions.add(transition);
    }

    public void addTransitions(Transition... transitions){
        for(Transition transition : transitions){
            this.transitions.add(transition);
        }
    }
    
    public void addTransitions(List<Transition> list){
    	Iterator<Transition> ite = list.listIterator();
        while(ite.hasNext()){
        	Transition tmp = ite.next();
        	if(!this.transitions.contains(tmp)) {
        		this.transitions.add(tmp);
        	}
        }
//        this.transitions.addAll(list);
    }
    
    public void addStates(List<State> list) {
    	Iterator<State> ite = list.listIterator();
        while(ite.hasNext()){
        	State tmp = ite.next();
        	if(!this.states.contains(tmp)) {
        		this.states.add(tmp);
        	}
        }
    }
    
    public State getFinalState(){
        return this.finalState;
    }

    public void setInitialState(State state, boolean changeState){
        this.initialState = (this.initialState == null || changeState)? state : this.initialState;
        state.setInitial(true);
    }

    public void setFinalState(State state, boolean changeState){
        this.finalState = (this.finalState == null || changeState)? state : this.finalState;  
        state.setFinal(true);
    }
    
    public void removeFinalState() {
    	this.finalState = null;
    }

    public int getNumberOfStates(){

        return this.states.size();
    }

    public List<State> getStates(){
        return this.states;
    }

    public List<Transition> getTransitions(){
        return this.transitions;
    }

    public State getInitialState(){
        return this.initialState;
    }

    public List<Integer> getFinalStates(){
        return this.finalStates;
    }

    public State getStateById(int id){
        for(int i=0; i<this.states.size(); i++){
            if(this.states.get(i).id == id) return this.states.get(i);        }
        return null;
    }

    public void deleteStateById(int id){
        for(int i=0; i<this.states.size(); i++){
            if(this.states.get(i).id == id) this.states.remove(i);       
        }        
    }

    public void  updateTransitions(int oldValue, int newValue){
        // System.out.println(oldValue + ":" + " " + oldValue + ", newValue: " + newValue);
        for(int i = 0; i < this.transitions.size(); i++){
            Transition transt = this.transitions.get(i);
            if(transt.getFrom() == oldValue){
                transt.setFrom(newValue);
            }
        }
    }

    public String toString(){
        return this.states.toString() + "\n" + this.transitions.toString();
    }
}