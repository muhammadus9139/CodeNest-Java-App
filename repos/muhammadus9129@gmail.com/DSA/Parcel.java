public class Parcel{
    private String code;
    private ParcelType type;
    private ParcelStatus status;
    


    Parcel(String code,ParcelType type,ParcelStatus status){
         this.code=code;
         this.type=type;
         this.status=status;
     
    }

    public String getcode(){
           return code;
     }

     public ParcelType gettype(){
       return type;
     }

     public ParcelStatus getstatus(){
         return status;
    }

     public String toString(){
       return String.format("%s %s %s",code,type,status);
     }
} 