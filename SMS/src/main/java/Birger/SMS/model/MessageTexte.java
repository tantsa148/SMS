package Birger.SMS.model;

public class MessageTexte {
    private String contenu;

    public MessageTexte() {}

    public MessageTexte(String contenu) {
        this.contenu = contenu;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public String toString() {
        return contenu;
    }
}

