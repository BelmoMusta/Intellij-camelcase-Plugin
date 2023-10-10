package musta.belmo.plugins.base64converter.ast;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.TextRange;
import musta.belmo.plugins.base64converter.util.TextUtils;

import java.util.Arrays;

public class CamelCaseTransformer implements Transformer {

    private final Editor editor;
    private final Document document;

    public CamelCaseTransformer(Editor editor) {
        this.editor = editor;
        document = editor.getDocument();
    }

    @Override
    public void transformPsi() {
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        int startFrom = selectionModel.getSelectionStart();

        if (selectedText == null) {
            CaretModel caretModel = editor.getCaretModel();
            int offset = caretModel.getOffset();
            startFrom = getStartOffset(offset);
            int endOffset = getEndOffset(offset);
            selectedText = document.getText(new TextRange(startFrom, endOffset));
        }
        String transformedText = transformText(selectedText);
        if (transformedText == null) {
            return;
        }
        document.replaceString(startFrom, startFrom + selectedText.length(), transformedText);
    }

    private int getStartOffset(int offset) {
        char current;
        int startOffset = offset;
        do {
            startOffset--;
            current = document.getText().charAt(startOffset);
        } while (isStringTokenDelimiter(current) && startOffset > 0);
        return startOffset + 1;
    }

    private int getEndOffset(int offset) {
        String text = document.getText();
        char current = text.charAt(offset);
        int startOffset = offset;

        while (isStringTokenDelimiter(current) && startOffset < editor.getDocument().getTextLength()) {
            startOffset++;
            current = text.charAt(startOffset);
        }
        return startOffset;
    }

    private boolean isStringTokenDelimiter(char current) {
        final String delimiters = "'\" -/{}[]:;*=+)(@!$%&|.,~\n\r\t";
        return !delimiters.contains(String.valueOf(current));
    }
    public static String camelCase(String input) {
        return Arrays.stream(input.split("[ \t]+|_"))
                .reduce((a, b) -> a + TextUtils.capitalize(b)).orElse(input);
    }

    private String transformText(String text) {
        if (text == null) {
            return null;
        }
        return camelCase(text);
    }

    @Override
    public String getActionName() {
        return "CamelCase";
    }

    @Override
    public boolean isApplied() {
        return true;
    }
    private static final String CAMELCASE_REGEX = "(?<!(^|[A-Z\\d]))((?=[A-Z\\d])|[A-Z](?=[\\d]))|(?<!^)" +
            "(?=[A-Z\\d][a-z])";

}
