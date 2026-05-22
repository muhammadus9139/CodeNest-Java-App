public class Slot{
    private String id;
    private Parcel parcels[];
    private boolean available;

    Slot(String id,boolean available,int n){
        this.id=id;
         this.available=available;
        parcels=new Parcel[n];
        for(int i=0;i<parcels.length;i++){
         // String cd=String.format("PKX-2025-%s",code);
         parcels[i]=new Parcel(id,ParcelType.REGULAR,ParcelStatus.IN_STORAGE);
        }
   

    public boolean storesafe(){
          if(available){
            available=false;
             return true;
          }
         else
             return false;
     }

    

     public boolean remove(){
          if(!available){
            available=true;
             return true;
          }

         else
             return false;
     }

     public String toString(){
         String st=available?"[]":"[F]";
         return String.format("%s %b",id,available);
     }
}