import java.util.Random;

public class Player{

    private String name;
    private Card hands[];
    private int cardsLeft;
    private int points;


    public Player(String name, int noOfCards){
        this.name = name;
        hands = new Card[noOfCards];
        cardsLeft = noOfCards;
        this.points = 0;
    }


    public void showhand(){
        System.out.println("\nCards of "+ this.name + "\n===================");

        for(int i = 0; i < hands.length;i++)
            System.out.println(hands[i]);

    }

    public void handSize(){
        System.out.println("\nCards of "+ this.name +": " +this.cardsLeft );
    }

    public boolean left(){
        if(this.cardsLeft == 0)
            return true;
        else
            return false;
    }

    public int calculateScore(int points){
        return this.points += points;
    }

    public int getCurrentScore(){
        return this.points;
    }

    public Card throwCard(){
        Random rand = new Random();

        Card c;
        int i = 0;

        while (true || this.cardsLeft != 0) {
            c = this.hands[rand.nextInt(hands.length - i)];

            if (c != null) {
                cardsLeft--;
                i++;
                break;
            } else{
                continue;
            }
        }
        return c;
    }


    public String getName(){
        return this.name;
    }

    public Card getHand(int index){
        return this.hands[index];
    }

    public void setHand(int index, Card c){
        this.hands[index] = c;
    }

    public int getCardsLeft(){
        return this.cardsLeft;
    }

    public void scoreSheet(int index){
        System.out.println("Player " + (index+1) + ": "  + this.points);
    }
}