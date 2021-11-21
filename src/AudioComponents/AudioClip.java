package AudioComponents;

import java.util.Arrays;

public class AudioClip {

    // members
    public static final double duration_ = 1;
    public static final int sampleRate_ = 44100;
    public static final int TOTAL_SAMPLES = (int)(sampleRate_ * duration_);
    private byte[] byteArray_;

    // constructor
    public AudioClip(){
        byteArray_ = new byte[(int)(TOTAL_SAMPLES * 2)];
        Arrays.fill(byteArray_, (byte) 0); // initial and fill it with 0
    }

    public void setValues(int index, int value){
        // Byte.toUnsignedInt( the signed number )
        byte mostSignificant = (byte) ((value >>> 8) & 0xff);
        byte lessSignificant = (byte) (value & 0xff);
        byteArray_[index * 2] = lessSignificant;
        byteArray_[index * 2 + 1] = mostSignificant;
    }

    public int getSample(int index){
        byte mostSignificant = byteArray_[index * 2 + 1];
        byte lessSignificant = byteArray_[index * 2];
        short value = (short) (((mostSignificant & 0xff) << 8) | (lessSignificant & 0xff));
        return value;
    }

    public byte[] getData(){
        byte[] bytes = Arrays.copyOf(byteArray_, byteArray_.length);
        return bytes;
    }

}