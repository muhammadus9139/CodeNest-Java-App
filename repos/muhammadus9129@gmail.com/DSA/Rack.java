public class Rack{
   private Slot[]slots;
   private String slotname;

    Rack(String slotname,int [] rowlength){ 
       this.slotname=slotname;
        slots=new Slot[rowlength.length];
         for(int i=0;i<rowlength.length;i++){
           slots[i]=new Slot("R3-S0"+(i+1),true,5);
         }
}

}

