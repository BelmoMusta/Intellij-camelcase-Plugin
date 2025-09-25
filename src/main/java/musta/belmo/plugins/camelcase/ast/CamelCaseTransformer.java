package musta.belmo.plugins.camelcase.ast;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public class CamelCaseTransformer implements Transformer {

    private final Editor editor;
    private final @Nullable Document document;

    public CamelCaseTransformer(@Nullable Editor editor) {
        this.editor = editor;
        document = Optional.ofNullable(editor)
                .map(Editor::getDocument)
                .orElseThrow();
    }

    @Override
    public void transformPsi() {
        if (document == null) {
            return;
        }
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();

        if (selectedText == null) {
           return;
        }
        int startFrom = selectionModel.getSelectionStart();
        String transformedText = transformText(selectedText);
        if (transformedText == null) {
            return;
        }
        document.replaceString(startFrom, startFrom + selectedText.length(), transformedText);
    }

    public static String camelCase(String input) {
        return Arrays.stream(input.split("[ \t-]+|_"))
                .reduce((a, b) -> a + capitalize(b)).orElse(input);
    }

    /**
     * uncapitalize the input String
     *
     * @param input @link String}
     * @return String
     */
    public static String capitalize(String input) {
        String output = input;
        if (input != null && !input.isEmpty()) {
            output = Character.toUpperCase(input.charAt(0)) + input.substring(1);
        }
        return output;
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
}
