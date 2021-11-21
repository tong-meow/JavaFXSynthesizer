package AudioComponents;

public class SineWave implements AudioComponent {

    // members
    public static short maxValue_ = Short.MAX_VALUE;
    public float frequency_;

    // default constructor
    SineWave(){
        frequency_ = 440;
    }

    // constructor with parameter of frequency
    public SineWave(float frequency){
        frequency_ = frequency;
    }

    // fill the audio clip with sine wave
    @Override
    public AudioClip getClip(){
        AudioClip audioClip = new AudioClip();
        for (int i = 0; i<(AudioClip.TOTAL_SAMPLES); i++){
            short value = (short) (maxValue_ * Math.sin(2 * Math.PI * this.frequency_ * i / AudioClip.sampleRate_));
            audioClip.setValues(i, value);
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

