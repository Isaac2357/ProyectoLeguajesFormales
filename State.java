
public class State {
    boolean isInitial = false;
    boolean isFinal = false;
    int id;
   
    public State(int index){
        this.id = index;
    }

    public int getId(){
        return this.id;
    }
    
    public String toString(){
        return "S: " + this.id + " (initial: " + isInitial + ", final:" + isFinal + ")";
    }

    public void setInitial(boolean init){
        this.isInitial = init;
    }

    public void setFinal(boolean isFinal){
        this.isFinal = isFinal;
    }

    public boolean getInitial(){
        return this.isInitial;
    }

    public boolean getFinal(){
        return this.isFinal;
    }
}