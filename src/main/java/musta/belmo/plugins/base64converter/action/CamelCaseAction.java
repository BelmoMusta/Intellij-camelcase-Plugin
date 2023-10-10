package musta.belmo.plugins.base64converter.action;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import musta.belmo.plugins.base64converter.ast.CamelCaseTransformer;
import musta.belmo.plugins.base64converter.ast.Transformer;

public class CamelCaseAction extends AbstractAction {

    @Override
    protected Transformer getTransformer() {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        return new CamelCaseTransformer(editor);
    }
}