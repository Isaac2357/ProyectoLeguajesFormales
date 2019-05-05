public  class Transition{
    private int from;
    private int to;
    private char character;

    public Transition(int from, int to, char character){
        this.from = from;
        this.to = to;
        this.character = character;
    }

    public int getFrom(){
        return this.from;
    }

    public void setFrom(int from){
        this.from = from;
    }

    public void setTo(int to){
        this.to = to;
    }
    public int getTo(){
        return this.to;
    }

    public char getCharacter(){
        return this.character;
    }

    public String toString(){
        return this.from + " -[" + this.character + "]-> "+this.to;
    }
}