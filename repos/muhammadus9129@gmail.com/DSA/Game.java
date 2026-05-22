public class Game{
    Player players[];
    Deck deck;
    Card PlayCards[];

    static int counter = 0;



    public Game(final int noOfPlayers){

        deck = new Deck();
        deck.display();
        deck.Shuffle();


        players = new Player[noOfPlayers];

        int count = 1;

        for(int  i = 0; i < players.length; i++)
            players[i] = new Player("Player "+ count++, 52/players.length);

        counter= 0;

    }

    public void distribute(){
        int count = 0;
        for(int i = 0; i < players.length; i++)
            for(int j = 0; j < 52/players.length; j++)
                players[i].setHand(j, deck.getCards(count++));
        for(int i = 0; i < players.length; i++)
            players[i].showhand();

    }


    public void playhand(){

        PlayCards = new Card[players.length];

        //Loop to throw a card for each player
        for(int i = 0; i < players.length; i++)
            PlayCards[i] = players[i].throwCard();

        //Display the thrown Cards
        System.out.println("\n---------Round " + (counter+1) + "---------");
        System.out.println("\nCards Thrown:");
        for(int i = 0; i < players.length; i++){

            System.out.println("Player " + (i+1)+ ": " + PlayCards[i]);
        }

        //Find the winner
        int roundWinner = getroundvictor(PlayCards);

        int points = 10;

        if(roundWinner <= players.length)
        {
            //Add points
            players[roundWinner].calculateScore(points);
        } else{
            System.out.println("\nTie! No points!");
        }

       //Display
        System.out.println("\n--------Score Board--------");
        //Call the method to display
        for(int i = 0; i < players.length; i++)
            players[i].scoreSheet(i);



        counter++;
    }


    public void getvictor(){

        int score[] = new int[players.length];

        for(int i = 0; i < players.length; i++)
            score[i] = players[i].getCurrentScore();


        int winner = 0;
        int highestScore = score[0];

        for(int i = 0; i < players.length; i++) {
            if (score[i] > highestScore) {

                highestScore = score[i];

                //Store the index of winner
                winner = i;
            }
            //Check for tie
            else if(!(winner == i))
                if(score[i] == highestScore)
                    winner = players.length + 1;
        }

        if(winner <= players.length)
        {
            System.out.println("\nCongratualtions! PLAYER "+ (winner+1) + " won!");
        }

        else
            System.out.println("\nTIE!");
        System.out.print("GAME OVER!");
    }

    public int getRoundCounter(){
        return counter;
    }



    public int getroundvictor(Card[] inPlayCards){

        //Array to store values
        int values[] = new int[players.length];

        for (int i = 1; i < inPlayCards.length; i++)
            values[i] = inPlayCards[i].getRank().getValue();


        int winner = 0;
        //Store 1st value as max
        int highestValue = inPlayCards[0].getRank().getValue();


        for(int i = 0;i < inPlayCards.length; i++){
            if(values[i] > highestValue){
                highestValue = values[i];

                //Store the index of winner
                winner = i;
            }
            //Check for tie
            else if(!(winner == i))
                if(values[i] == highestValue)
                    winner = players.length + 1;
        }
        return winner;
    }

}