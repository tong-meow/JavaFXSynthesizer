package AudioComponents;

public class VFSineWave implements AudioComponent{

    public static short maxValue_ = Short.MAX_VALUE;
    private AudioClip inputClip_;
    private AudioClip outputClip_;

    // default constructor
    public VFSineWave(){
        inputClip_ = null;
        outputClip_ = new AudioClip();
    }

    @Override
    public AudioClip getClip() {
        return outputClip_;
    }

    @Override
    public boolean hasInput() {
        return inputClip_ != null ;
    }

    @Override
    public void connectInput(AudioComponent input) {
        inputClip_ = input.getClip();
        if (hasInput()) {
            double phase = 0;
            for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
                phase += 2 * Math.PI * inputClip_.getSample(i) / AudioClip.sampleRate_;
                int value = Clamp((int) (maxValue_ * Math.sin(phase)));
                value = Clamp(value);
                outputClip_.setValues(i, value);
            }
        }
        inputClip_ = null;
    }

    public int Clamp (int num){
        if (num > Short.MAX_VALUE ){
            num = Short.MAX_VALUE;
        }else if (num < Short.MIN_VALUE){
            num = Short.MIN_VALUE;
        }
        return num;
    }
}

