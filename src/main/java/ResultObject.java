/**
 * Created by vlad on 17.10.15.
 */
public class ResultObject {
    private double enthropyIn;
    private double enthropyOut;
    private double bitPerSymbol;
    private double time;

    public ResultObject(double enthropyIn, double enthropyOut, double bitPerSymbol, double time){
        this.enthropyIn = enthropyIn;
        this.enthropyOut = enthropyOut;
        this.bitPerSymbol = bitPerSymbol;
        this.time = time;
    }

    public double getEnthropyIn() {
        return enthropyIn;
    }

    public double getEnthropyOut() {
        return enthropyOut;
    }

    public double getBitPerSymbol() {
        return bitPerSymbol;
    }

    public double getTime() {
        return time;
    }
}
