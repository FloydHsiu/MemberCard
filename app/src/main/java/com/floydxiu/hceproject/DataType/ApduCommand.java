package com.floydxiu.hceproject.DataType;

/**
 * Created by Floyd on 2016/11/27.
 */

public class ApduCommand {
    static public byte[] GET_TRANSCODE_COMMAND = {
            (byte)0x00,
            (byte)0xA4,
            (byte)0x04,
            (byte)0x00,
            (byte)0x07,
            (byte)0xF0, (byte)0x39, (byte)0x41, (byte)0x48, (byte)0x14, (byte)0x81, (byte)0x00
    };

    static public byte[] APDU_COMMAND2 = {
            (byte)0x00,
            (byte)0xA4,
            (byte)0x04,
            (byte)0x00,
            (byte)0x07,
            (byte)0xF0, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06
    };

    static public byte[] TRANS_SUCCESS_COMMAND = {
            (byte)0x00,
            (byte)0x01,
            (byte)0x00,
            (byte)0x01
    };

    static public Byte[] TRANS_FAIL_COMMAND = {
            (byte)0x00,
            (byte)0x01,
            (byte)0x00,
            (byte)0x00
    };

    public int step;
    public int state;

    public ApduCommand(byte[] cmd){
        initailizeApduCommand(cmd);
    }

    private void initailizeApduCommand(byte[] cmd){
        if(cmd.length == 12){
            step = 0;
            state = 0;
        }
        else if(cmd.length == 4){
            step = 1;
            if(cmd[3] == 0x01){
                state = 1;
            }
            else{
                state = 0;
            }
        }
        else{
            step = -1;
            state = -1;
        }
    }
}
