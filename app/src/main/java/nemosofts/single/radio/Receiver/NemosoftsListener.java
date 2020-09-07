package nemosofts.single.radio.Receiver;

/**
 * Created by thivakaran
 */

public interface NemosoftsListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message);
}