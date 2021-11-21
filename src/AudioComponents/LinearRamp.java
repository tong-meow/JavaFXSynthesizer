package AudioComponents;

public class LinearRamp implements AudioComponent {

    private float start_;
    private float stop_;
    // private AudioClip clip_;

    public LinearRamp(){
        start_ = 50;
        stop_ = 2000;
    }

    public LinearRamp(float start, float stop){
        start_ = start;
        stop_ = stop;
    }

    @Override
    public AudioClip getClip() {
        AudioClip audioClip = new AudioClip();
        for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            int sample = (int) ((start_ * (AudioClip.TOTAL_SAMPLES - i) + stop_ * i) / AudioClip.TOTAL_SAMPLES);
            audioClip.setValues(i, sample);
        }
        return audioClip;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) { }
}
