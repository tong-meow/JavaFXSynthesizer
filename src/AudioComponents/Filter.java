package AudioComponents;

public class Filter implements AudioComponent {

    double scale_;
    private AudioClip inputClip_;
    private AudioClip outputClip_;

    public Filter (double scale){
        scale_ = scale;
        inputClip_ = null;
        outputClip_ = new AudioClip();
    }

    @Override
    public AudioClip getClip(){
        return outputClip_;
    }

    @Override
    public boolean hasInput(){
        return inputClip_ != null;
    }

    @Override
    public void connectInput(AudioComponent input) {
        inputClip_ = input.getClip();
        if (hasInput()) {
            for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
                int value = inputClip_.getSample(i);
                value *= scale_;
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
