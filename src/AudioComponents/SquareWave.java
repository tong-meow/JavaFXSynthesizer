package AudioComponents;

public class SquareWave implements AudioComponent{

    // members
    public static short maxValue_ = Short.MAX_VALUE;
    public float frequency_;

    // default constructor
    SquareWave(){
        frequency_ = 440;
    }

    // constructor with parameter of frequency
    public SquareWave(float frequency){
        frequency_ = frequency;
    }

    @Override
    public AudioClip getClip() {
        AudioClip audioClip = new AudioClip();
        for (int i = 0; i < (AudioClip.TOTAL_SAMPLES); i++) {
            if ((frequency_ * i / AudioClip.sampleRate_) % 1 > 0.5) {
                audioClip.setValues(i, maxValue_);
            } else {
                audioClip.setValues(i, maxValue_ * -1);
            }
        }
        return audioClip;
    }


    @Override
    public boolean hasInput(){
        return false;
    }

    @Override
    public void connectInput(AudioComponent input){ }
}

