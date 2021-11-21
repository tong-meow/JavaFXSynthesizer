package AudioComponents;

public class Mixer implements AudioComponent {

    private AudioClip inputClip_;
    private AudioClip outputClip_;

    public Mixer(){
        inputClip_ = null;
        outputClip_ = new AudioClip();
    }

    @Override
    public AudioClip getClip() {
        // if there is no input, Mixer.getClip will return a unfilled clip.
        return outputClip_;
    }

    @Override
    public boolean hasInput() {
        return inputClip_!= null;
    }

    @Override
    public void connectInput(AudioComponent input) {
        // make the input clip softer
//            AudioComponent filter = new Filter(0.1);
//            filter.connectInput(input);
        inputClip_ = input.getClip();
        if (hasInput()) {
            for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
                int value = outputClip_.getSample(i) + inputClip_.getSample(i);
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
