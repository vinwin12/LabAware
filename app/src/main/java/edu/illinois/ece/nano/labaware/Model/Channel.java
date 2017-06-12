package edu.illinois.ece.nano.labaware.Model;

public class Channel {

    int xdistance;
    int ydistance;
    int xposition;
    int yposition;
    int channelNumber = 0;



    Channel(int channel,boolean isFirst){
        xdistance = locationofChannels(100);
        if(isFirst){
            ydistance = locationofChannels(channel);
        }
        else{
            ydistance = locationofChannelsSecondSquare(channel);
        }
        channelNumber = channel;

    }


    public void setPosition(int x, int y){
        xposition = x;
        yposition = y;
    }

    public int getXPosition(){
        return xposition;
    }

    public int getYposition(){
        return yposition;
    }

    public void calculatePosition(int fromY, String operation, int xvalue){
        if (operation.equals("+")){
            yposition = fromY + ydistance;

        }

        else{
            yposition = fromY - ydistance;
        }

        xposition = xvalue - xdistance;

    }


    public int getXdistance(){
        return xdistance;

    }

    public int getYdistance(){
        return ydistance;
    }

    public void setDistance(int x, int y){
        xdistance = x;
        ydistance = y;
    }



    public int locationofChannels(int request){


        int num = 0;
        if (request == 100){
            num = 40;
        }

        else if(request == 1){
            num = 93;
        }

        else if(request == 2){
            num = 71;
        }

        else if(request == 3){
            num = 47;
        }

        else if(request == 4){
            num = 23;
        }

        else if(request == 5){
            num = 0;
        }

        else if(request == 6){
            num = 23;
        }

        else if(request == 7){
            num = 48;
        }

        else if(request == 8){
            num = 71;
        }

        else if(request == 9){
            num = 94;
        }

        else if(request == 10){
            num = 118;
        }

    return num;

    }


    public int locationofChannelsSecondSquare(int request){

        int num = 0;
        if (request == 100){
            num = 40;
        }

        else if(request == 1){
            num = 120;
        }

        else if(request == 2){
            num = 95;
        }

        else if(request == 3){
            num = 72;
        }

        else if(request == 4){
            num = 49;
        }

        else if(request == 5){
            num = 25;
        }

        else if(request == 6){
            num = 0;
        }

        else if(request == 7){
            num = 23;
        }

        else if(request == 8){
            num = 47;
        }

        else if(request == 9){
            num = 69;
        }

        else if(request == 10){
            num = 92;
        }

        return num;




    }


}
