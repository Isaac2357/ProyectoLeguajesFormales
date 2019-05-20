import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.TreeSet;

public class M5 {

    private static boolean[] visitedFinal = null;
    private static TreeSet<Pair> pairs = new TreeSet<Pair>();
    private static ArrayList<Set<Integer>> sets = new ArrayList<>();


    private static class Pair implements Comparable{

        private int stateOne;
        private int stateTwo;

        public Pair(int stateOne, int stateTwo){
            if(stateOne <= stateTwo){
                this.setStateOne(stateOne);
                this.setStateTwo(stateTwo);
            }else{
                this.setStateOne(stateTwo);
                this.setStateTwo(stateOne);
            }

        }

        public int getStateOne(){
            return stateOne;
        }

        public int getStateTwo(){
            return stateTwo;
        }

        public void setStateOne(int stateOne){
            this.stateOne = stateOne;
        }

        public void setStateTwo(int stateTwo){
            this.stateTwo =  stateTwo;
        }

        public String toString(){
            return "(" + getStateOne() + ", " + getStateTwo()  + ")";
        }

        @Override
        public int compareTo(Object o) {
            if(o instanceof Pair){
                return this.getStateOne()*1000 - ((Pair)o).getStateOne()*1000 +
                       this.getStateTwo() - ((Pair)o).getStateTwo();
            }
            return -1;
        }

    }

    public static void minimize(DFA automaton){
        DFS(automaton.getInitialState().getId(), automaton.getTransitions(), new boolean[automaton.getNumberOfStates()]);
       
        deleteUnreachableStates(automaton);
        
        Pair[][] p = cartesianProduct(automaton.getStates(), automaton.getAlphabet());
       
        for (int i = 0; i < p.length; i++) {
            int stateOne = p[i][0].getStateOne();
            int stateTwo = p[i][0].getStateTwo();
            for(int j = 1; j < automaton.getAlphabet().size() + 1; j++){
                int transOne = automaton.transitionsFunction(stateOne, automaton.getAlphabet().get(j-1));
                int transTwo = automaton.transitionsFunction(stateTwo, automaton.getAlphabet().get(j-1));
                Pair toAdd = new Pair(transOne, transTwo);
                p[i][j] = toAdd;
                pairs.add(toAdd);
            }

        }

        eliminate(p, automaton);
        removeTransitivity(pairs);
        mergeEquivalentStates(automaton);
        automaton.deleteRepeatedTransitions();

        System.out.println("-------------------------------------------");
        System.out.println("Estados equivalentes");


        for(Pair a : pairs){
            System.out.println(a);
        }
        for(Set<Integer> s : sets){
            System.out.println(s);
        }

        System.out.println("-------------------------------------------");
        
        for(State s : automaton.getStates()){
            System.out.println(s);
        }
        for(Transition t : automaton.getTransitions()){
            System.out.println(t);
        }

    }

    private static void mergeEquivalentStates(DFA automaton) {
        //get set with equivalent states
        for(int i = 0; i< sets.size(); i++){
            Set<Integer> set = sets.get(i);
            int j = 0;
            int principalState = 0;
            //Merge states of each set
            for(Integer state : set){
                if(j == 0){
                    principalState = state;
                }else{
                    automaton.updateTransitions(state, principalState);
                    automaton.deleteStateById(state);

                    for(int k=0; k < automaton.getTransitions().size(); k++){
                        Transition aux = automaton.getTransitions().get(k);
                        if(aux.getTo() == state){
                            automaton.updateTransitionByTo(aux.getFrom(), aux.getTo(), principalState);
                        }
                    }
                    // n -> state
                    // n -> principalState
                }
                j++;
            }
        }
    }

    private static void eliminate(Pair[][] p, DFA automaton) {
       
        boolean pairsSetChanged = false;

        for(int i=0; i < p.length; i++){    
            Pair tmp = p[i][0];
            if(automaton.getStateById(tmp.stateOne).getFinal() && !automaton.getStateById(tmp.stateTwo).getFinal()){
                pairs.remove(tmp);
            }else if(!automaton.getStateById(tmp.stateOne).getFinal() && automaton.getStateById(tmp.stateTwo).getFinal()){
                pairs.remove(tmp);
            }
        }

        do{
            for(int i = 0; i < p.length; i++){
                Pair pair = p[i][0];
                for(int j = 1; j < p[i].length; j++){
                    Pair aux = p[i][j];
                    if(pairs.contains(pair)){
                        if(!pairs.contains(aux)){
                            pairs.remove(pair);
                            pairsSetChanged = true;
                            break;
                        }
                    }else{
                        pairsSetChanged = false;
                        break;
                    }
                }
            }
        }while(pairsSetChanged);

        TreeSet<Pair> aux = new TreeSet<Pair>();
        for(Pair a : pairs){
            if(a.getStateOne() == a.getStateTwo()){
                continue;
            }else{
                aux.add(a);
            }
        }
        pairs = aux;

    }
    
    private static Pair[][] cartesianProduct(List<State> states, List<Character> alphabet){
       int m = gauss(states.size() - 1) , n = alphabet.size() + 1;
        Pair[][] p = new Pair[m][n] ;  
        int aux = 0;

        for(int i = 0; i < m; i++){
            for(int j = i +1; j < states.size(); j++){
                Pair toAdd = new Pair( states.get(i).getId(), states.get(j).getId() ); 
                p[aux][0] = toAdd;
                pairs.add(toAdd);
                aux++;
            }
                           
        }
        return p;
    }

    private static int gauss(int n){
        return (n * (n + 1)) / 2;
    }

    private static void DFS(int state, List<Transition> transitions, boolean[] visited){
        visited[state] = true;
        Iterator<Transition> ite = transitions.listIterator(); 
        while (ite.hasNext()) { 
            Transition next = ite.next(); 
            if(next.getFrom() == state){
                if (!visited[next.getTo()]) 
                DFS(next.getTo(), transitions, visited); 
            }
        } 
        visitedFinal = visited;
    }

    private static void deleteUnreachableStates(DFA automaton){
        if(visitedFinal == null) return;

        for(int i = 0; i < visitedFinal.length; i++){
            if(!visitedFinal[i]){
                automaton.deleteStateById(i);
                automaton.deleteTransitionsByFrom(i);
            }
        }

    }

    private static void removeTransitivity(TreeSet<Pair> pairs){
        boolean addToSet = false;
        for(Pair p : pairs){
            if(sets.size() == 0){
                Set<Integer> set = new TreeSet<>();
                set.add(p.getStateOne());
                set.add(p.getStateTwo());
                sets.add(set);
            }else{
                for(int i = 0; i < sets.size(); i++){
                    Set<Integer> aux = sets.get(i);
                    if(aux.contains(p.getStateOne()) || aux.contains(p.getStateTwo())){
                        aux.add(p.getStateOne());
                        aux.add(p.getStateTwo());
                        addToSet = true;
                        break;
                    }
                }

                if(!addToSet){
                    Set<Integer> setToAdd = new TreeSet<>();
                    setToAdd.add(p.getStateOne());
                    setToAdd.add(p.getStateTwo());
                    sets.add(setToAdd);
                }

                addToSet = false;

            }
        }
    }

    public static void main(String[] args) {
        //test class here
        DFA a = new DFA();
        a.addStates(new State(0), new State(1), new State(2), new State(3),new State(4), new State(5), new State(6), new State(7));
        a.setInitialState(a.getStateById(0), true);
        a.setFinalState(a.getStateById(2), true);
        
        
        a.addSymbolToAlphabet('a');
        a.addSymbolToAlphabet('b');

        a.addTransitions(new Transition(0,1,'a'),
                        new Transition(0,5,'b'),
                        new Transition(1,6,'a'),
                        new Transition(1,2,'b'),
                        new Transition(2,0,'a'),
                        new Transition(2,2,'b'),
                        new Transition(3,2,'a'),
                        new Transition(3,6,'b'),
                        new Transition(4,7,'a'),
                        new Transition(4,5,'b'),
                        new Transition(5,2,'a'),
                        new Transition(5,6,'b'),
                        new Transition(6,6,'a'),
                        new Transition(6,4,'b'),
                        new Transition(7,6,'a'),
                        new Transition(7,2,'b'));  
                        
         minimize(a);   
   
    }


}