// Interface Panel
public interface IPanel {
    // 화면 보일 때 호출
    void onShow();
    // 화면 꺼질 때 호출
    void onHide();

    javax.swing.JPanel getPanel();
}
