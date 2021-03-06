import java.util.Stack;

public class M2{

    private static class ReIndexes{
        int begin = 0;
        int end = 0;

        public ReIndexes(int begin, int end){
            this.begin = begin;
            this.end = end;
        }

        public void setBegin(int begin){
            this.begin = begin;
        }
        public void setEnd(int end){
            this.end = end;
        }

        public int getBegin(){
            return this.begin;
        }

        public int getEnd(){
            return this.end;
        }

        public String toString(){
            return "(" + this.begin + "," + this.end + ");";
        }

    }
    
    public static DFA thompson(String regex){
        DFA automaton = new DFA();
        Stack<ReIndexes> regularExpressions = new Stack<ReIndexes>();
        ReIndexes secondReIndexes;
        ReIndexes firstReIndexes;

        int from1 = 0, to1 = 0, from2 = 0, to2 = 0;

        for(int i = 0; i < regex.length(); i++){                
            
            char character = regex.charAt(i);
            
            if(isOperator(character)){
                //Si es '-', concatenar ambos automatas
                //Unir to1 con from2, de manera que 
                //su combinacion ya nos sea final
                //from2, quitar inicial
                if(character == '-'){
                    //Get indexes of the regular expressions that will be concatenated
                    secondReIndexes = regularExpressions.pop();
                    firstReIndexes = regularExpressions.pop();

                    // automaton.deleteStateById(from2); changed to -->
                    automaton.deleteStateById(secondReIndexes.getBegin());
                    
                    // State to1State = automaton.getStateById(to1);
                    // to1State.setFinal(false);

                    automaton.getStateById(firstReIndexes.getEnd()).setFinal(false);
                    // automaton.setFinalState(automaton.getStateById(to2), true ); changed to -->
                    automaton.setFinalState(automaton.getStateById(secondReIndexes.getEnd()), true );

                    //buscar todas las transiciones donde from sea from2, y actualizarlas para
                    //que sean en vez de from2 => to1.

                    // automaton.updateTransitions(from2, to1);
                    // automaton.updateTransitions(from2, to1); changed to -->
                    automaton.updateTransitions(secondReIndexes.getBegin(), firstReIndexes.getEnd());
                    automaton.updateTransitions(secondReIndexes.getBegin(), firstReIndexes.getEnd());
                    
                    //Add new regular expression indexes to the Stack
                    regularExpressions.add(new ReIndexes(firstReIndexes.getBegin(), secondReIndexes.getEnd()));

                }else if(character == '|'){

                    secondReIndexes = regularExpressions.pop();
                    firstReIndexes = regularExpressions.pop();

                    State newInitialState = new State(DFA.IDCOUNTER++);
                    State newFinalState = new State(DFA.IDCOUNTER++);
                    
                    // automaton.addTransitions(
                    //     new Transition(newInitialState.getId(), automaton.getInitialState().getId(), '&'), 
                    //     new Transition(newInitialState.getId(), automaton.getStateById(from2).getId(), '&'),
                    //     new Transition(automaton.getFinalState().getId(), newFinalState.getId(), '&'),
                    //     new Transition(automaton.getStateById(to2).getId(), newFinalState.getId(), '&')
                    //     );

                        automaton.addTransitions(
                            new Transition(newInitialState.getId(), automaton.getStateById(firstReIndexes.getBegin()).getId(), '&'), 
                            new Transition(newInitialState.getId(), automaton.getStateById(secondReIndexes.getBegin()).getId(), '&'),
                            new Transition(automaton.getStateById(firstReIndexes.getEnd()).getId(), newFinalState.getId(), '&'),
                            new Transition(automaton.getStateById(secondReIndexes.getEnd()).getId(), newFinalState.getId(), '&')
                            );

                    automaton.addStates(newInitialState, newFinalState);
                    //quitar isInitial de from2, y quitar isFinal de to2
                    // automaton.getStateById(from2).setInitial(false);
                    // automaton.getStateById(to1).setFinal(false);
                    // automaton.getStateById(to2).setFinal(false);

                    
                    automaton.getStateById(secondReIndexes.getBegin()).setInitial(false);
                    automaton.getStateById(secondReIndexes.getEnd()).setFinal(false);

                    automaton.getStateById(firstReIndexes.getBegin()).setInitial(false);
                    automaton.getStateById(firstReIndexes.getEnd()).setFinal(false);

                    automaton.setInitialState(newInitialState, true);
                    automaton.setFinalState(newFinalState, true);

                    
                    // from2 = newInitialState.getId();
                    // to2 = newFinalState.getId();

                    //Add new regular expression indexes to the Stack
                    regularExpressions.add(new ReIndexes(newInitialState.getId(), newFinalState.getId()));

                }
                else if(character == '*'){
                    State newInitialState = new State(DFA.IDCOUNTER++);
                    State newFinalState = new State(DFA.IDCOUNTER++);
                    firstReIndexes = regularExpressions.pop();
                    automaton.addTransitions(new Transition(firstReIndexes.getEnd(),
                                            firstReIndexes.getBegin(),
                                            '&'),
                                            new Transition(newInitialState.getId(),
                                                           firstReIndexes.getBegin(),
                                                           '&'),
                                            new Transition(firstReIndexes.getEnd(),
                                                           newFinalState.getId(),
                                                           '&'),
                                            new Transition(newInitialState.getId(),
                                            newFinalState.getId(), '&'));
                    if(automaton.getFinalState().getId() == firstReIndexes.getEnd()){
                        automaton.getFinalState().setFinal(false);
                        automaton.setFinalState(newFinalState, true);
                    }
                    if(automaton.getInitialState().getId() == firstReIndexes.getBegin()){
                        automaton.getInitialState().setInitial(false);
                        automaton.setInitialState(newInitialState, true);
                    }
                    // automaton.getFinalState().setFinal(false);
                    // automaton.getInitialState().setInitial(false);
                    // automaton.setInitialState(newInitialState, true);
                    // automaton.setFinalState(newFinalState, true);
    
                    automaton.addStates(newInitialState, newFinalState);

                    automaton.getStateById(firstReIndexes.getBegin()).setInitial(false);
                    automaton.getStateById(firstReIndexes.getEnd()).setFinal(false);


                    regularExpressions.add(new ReIndexes(newInitialState.getId(), newFinalState.getId()));

                }else if(character == '+'){
                    firstReIndexes = regularExpressions.pop();
                    automaton.addTransition(new Transition(firstReIndexes.getEnd(), 
                                                            firstReIndexes.getBegin(),
                                                            '&'));
                    regularExpressions.add(firstReIndexes);
                }
                //Si es '|', unir creando nuevo estado inicial y nuevo
                //estado final, de manera que se creen la stransiciones 
                //epsilon                
                //Si es cerradura positiva, añadir nuevo estado inicial
                //y nuevo estado final, y enlazarlos el nuevoInt al viejoInit
                //ademas lo del nuevoFinal al viejoFinal
            
            }else{
                from1 = from2;
                to1 = to2;
                // Añadir estado a, que vaya con character a estado b
                from2 = DFA.IDCOUNTER++;
                to2 = DFA.IDCOUNTER++;
                State from = new State(from2);
                from.setInitial(true);
                State to = new State(to2);
                to.setFinal(true);
                automaton.addStates(from, to);
                automaton.setInitialState(from, from2 == 0);
                automaton.setFinalState(to, to2 == 1);
                automaton.addTransition(new Transition(from.getId(), to.getId(), character));

                //Add indexes of regular expression to the Stack
                regularExpressions.add(new ReIndexes(from.getId(), to.getId()));
            }
            
            System.out.println("ite: " + i + " : " + regularExpressions.toString() + " - " + character);
            // System.out.println(automaton.toString());
            System.out.println("-----------------------------------------------------------------");
        }
        return automaton;
    }
    
    private static boolean isOperator(char op){
        //         union    Kleen       cerr. pos.    concat.
        return op == '|' || op == '*' || op == '+' || op == '-';

        
    }
        

    public static void main(String[] args){
        DFA a = M2.thompson("ab*a*|-b-a*-");
        System.out.println(a.getStates().size());
        for(State s : a.getStates()){
            System.out.println(s);
        }
        for(Transition t : a.getTransitions()){
            System.out.println(t);
        }
    }    

}

