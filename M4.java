import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class M4 {
	
	private static class StateM4{
		public String state;
		public int id;
		public boolean isFinal;
		public StateM4(String state, int id, boolean isFinal) {
			this.state = state;
			this.id = id;
			this.isFinal = isFinal;
		}
		
		public String toString() {
			return '[' + this.state + ", " + this.id + ", " + this.isFinal + ']';
		}
	}
	
	
	public static void AFDConverter(DFA automaton) {
		List<Transition> transitions = automaton.removeAllTransitions();
		HashMap<String, Integer> newStates = new HashMap<>();
		//Process the current existing states:
		Stack<StateM4> statesToProcess = new Stack<>();
		//look through the alphabet, and then
		//iterate through transitions, and
		//look if the from and character 
		//are equal
		int idFinalState = automaton.getFinalState().getId();
		Iterator<State> iteStates = automaton.getStates().listIterator();
		while(iteStates.hasNext()) {
			State state = iteStates.next();
//			statesToProcess.remove(state.getId() + "");
			statesToProcess.removeIf(n -> n.id == state.getId());
			newStates.putIfAbsent(state.getId() + "", state.getId());
			Iterator<Character> iteAlphabet = automaton.getAlphabet().listIterator();
			
			while(iteAlphabet.hasNext()) {
				char symbol = iteAlphabet.next();
				Iterator<Transition> iteTrans = transitions.listIterator();
				String newState = "";
				int stateCount = 0;
				while(iteTrans.hasNext()) {
					Transition trans = iteTrans.next();
					if(trans.getFrom() == state.getId() && trans.getCharacter() == symbol) {
						newState += trans.getTo();
						stateCount++;
						System.out.println("trans: " + trans);
					}
				}
				char tempArray[] = newState.toCharArray(); 
				Arrays.sort(tempArray);
				newState = new String(tempArray);
				
				if(stateCount > 0) {
					int stateID = -1;
					
					if(!newStates.containsKey(newState)) {
						if(stateCount > 1) {
							stateID = DFA.IDCOUNTER++;
						} else {
							System.out.println("hola  " + newState);
							stateID = Integer.parseInt(newState);
						}
						automaton.addTransition(
								new Transition(state.getId(), stateID, symbol));
						newStates.put(newState, stateID);
						statesToProcess.add(new StateM4(newState, stateID, newState.contains(idFinalState + "")));
					} else {
						automaton.addTransition(
								new Transition(state.getId(), newStates.get(newState), symbol));
					}
					
//					automaton.addState(new State(stateID));
					
					
//					statesToProcess.add(new StateM4(newState, stateID, newState.contains(idFinalState + "")));
				}
				
			}
		}
		System.out.println("PILA: \n" + statesToProcess);
		System.out.println("MAPA: \n" + newStates);
		while(!statesToProcess.isEmpty()) {
			StateM4 tmpState = statesToProcess.pop();
			State tmp = new State(tmpState.id);
			tmp.setFinal(tmpState.isFinal);
			automaton.addStateAndFinal(tmp);
			Iterator<Character> iteAlphabet = automaton.getAlphabet().listIterator();
			
			while(iteAlphabet.hasNext()) {
				char symbol = iteAlphabet.next();
				Iterator<Transition> iteTrans = transitions.listIterator();
				String newState = "";
				int stateCount = 0;
				while(iteTrans.hasNext()){
					
					Transition trans = iteTrans.next();
					for(int i = 0; i < tmpState.state.length(); i++) {
						int id = Character.getNumericValue(tmpState.state.charAt(i));
//						System.out.println(id);
						if(trans.getFrom() == id && trans.getCharacter() == symbol &&
								!newState.contains(trans.getTo() + "")) {
							newState += trans.getTo();
							stateCount++;
						}
					} //endFor
				}//endWhile
				char tempArray[] = newState.toCharArray(); 
				Arrays.sort(tempArray);
				newState = new String(tempArray);
				System.out.println(newState);
				if(stateCount > 0) {
					int stateID = -1;
					
					if(!newStates.containsKey(newState)) {
						if(stateCount > 1) {
							stateID = DFA.IDCOUNTER++;
						} else {
							System.out.println("hola  " + newState);
							stateID = Integer.parseInt(newState);
						}
						automaton.addTransition(
								new Transition(tmpState.id, stateID, symbol));
						newStates.put(newState, stateID);
						statesToProcess.add(new StateM4(newState, stateID, newState.contains(idFinalState + "")));
					} else {
						automaton.addTransition(
								new Transition(tmpState.id, newStates.get(newState), symbol));
					}
					
//					automaton.addState(new State(stateID));
					
					
//					statesToProcess.add(new StateM4(newState, stateID, newState.contains(idFinalState + "")));
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DFA a = M2.thompson("ab*-c-*");
		M3.epsilonRemover(a);
		M4.AFDConverter(a);
		for(State s : a.getStates()){
            System.out.println(s);
        }
		System.out.println("entra");
        for(Transition t : a.getTransitions()){
            System.out.println(t);
        }
	}

}
