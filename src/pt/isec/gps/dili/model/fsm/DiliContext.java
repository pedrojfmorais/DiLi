package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.MessageType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DiliContext {

    /**
     * constante identificativa para alteração ao estado atual
     */
    public static final String PROP_FASE = "_FASE_";
    public static final String PROP_BOOK = "_LIVROS_";
    public static final String PROP_USER = "_USER_";

    /**
     * objeto que permite atualização automática dos dados na interface gráfica
     */
    PropertyChangeSupport pcs;

    public DiLi getData() {
        return data;
    }

    /**
     * modelo de dados utilizado
     */
    private DiLi data;

    /**
     * estado atual
     */
    private IDiliState state;

    /**
     * instância da própria classe, padrão singleton
     */
    private static DiliContext instance = null;

    /**
     * permite obter a instância da classe, padrão singleton
     *
     * @return instância única da classe
     */
    public static DiliContext getInstance() throws SQLException {
        if (instance == null)
            instance = new DiliContext();
        return instance;
    }

    /**
     * construtor privado
     */
    private DiliContext() throws SQLException {
        init(DiliState.LOGIN);
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * função que inicializa os dados
     *
     * @param state - estado inicial da aplicação
     */
    public void init(DiliState state) throws SQLException {
        this.data = new DiLi();
        this.state = state.createState(this, data);
    }

    /**
     * função que permite adicionar um listener a uma certa propriedade
     *
     * @param property - propriedade
     * @param listener - ação a realizar
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    /**
     * alterar o estado atual
     *
     * @param state - novo estado
     */
    public void changeState(IDiliState state) {
        this.state = state;
        pcs.firePropertyChange(PROP_FASE, null, null);
    }

    /**
     * obter o estado atual
     *
     * @return estado atual
     */
    public DiliState getState() {
        return state.getState() == null ? null : state.getState();
    }

    public Message login(String email, String password) {
        return state.login(email, password);
    }

    public void logout() {
        state.logout();
    }

    public void voltar() {
        state.voltar();
    }

    public Message createLibrarian(String name, String email, String password) {
        return state.createLibrarian(name, email, password);
    }

    public Message updateLibrarianInfo(String name, String email, String password) {
        Message message = state.updateLibrarianInfo(name, email, password);
        if (message.getType() == MessageType.SUCCESS)
            pcs.firePropertyChange(PROP_USER, null, null);
        return message;
    }

    public void changeBookVisibility(int idLivro) {
        state.changeBookVisibility(idLivro);
        pcs.firePropertyChange(PROP_BOOK, null, null);
    }

    public void removeBook(int idLivro) {
        state.removeBook(idLivro);
        pcs.firePropertyChange(PROP_FASE, null, null);
        pcs.firePropertyChange(PROP_BOOK, null, null);
    }

    public Message addBook(String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink, String imagePath){

        Message message = state.addBook(title, author, synopsis, language, genres, availability,
                costPerDownload, downloadLink, imagePath);

        if (message.getType() == MessageType.SUCCESS)
            pcs.firePropertyChange(PROP_BOOK, null, null);

        return message;
    }

    public Message updateBookInfo(int id, String title, String author, String synopsis, String language,
                                  List<String> genres, boolean availability, double costPerDownload,
                                  Map<String, String> downloadLink, String imagePath){

        Message message = state.updateBookInfo(id, title, author, synopsis, language, genres, availability,
                costPerDownload, downloadLink, imagePath);

        if (message.getType() == MessageType.SUCCESS)
            pcs.firePropertyChange(PROP_BOOK, null, null);

        return message;
    }
}
