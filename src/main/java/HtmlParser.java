import javax.swing.text.html.*;

public class HtmlParser extends HTMLEditorKit {

    public HTMLEditorKit.Parser getParser()
        {
            return super.getParser();
        }
}
