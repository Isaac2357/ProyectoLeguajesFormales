public class InterfaceHandler{
    
    private  String regularExpression;
    private  String text;

    public void setRegularExpression(String regularExpression){
       this.regularExpression = regularExpression;
    }

    public String getRegularExpression(){
        return this.regularExpression;
     }

     public void setText(String text){
        this.text = text;
     }
     
     public String getText(String regularExpression){
         return this.text;
      }

      public String compute(){
        //User modules here
        return "";
      }
}