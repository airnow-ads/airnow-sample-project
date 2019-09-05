package airnow.demo.ads;

/*
Demo item class used in the Infeed Banner example
 */

public class FeedItem {
    private int drawableRes;
    private String title;
    private String text;

    public FeedItem(int drawableRes, String title, String text) {
        this.drawableRes = drawableRes;
        this.title = title;
        this.text = text;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
