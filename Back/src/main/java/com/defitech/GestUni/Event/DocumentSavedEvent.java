package com.defitech.GestUni.Event;

import com.defitech.GestUni.models.Chahib.Document;
import org.springframework.context.ApplicationEvent;

/**
 * Événement qui est déclenché lorsque qu'un document est sauvegardé.
 * Cet événement permet de notifier d'autres composants du système pour qu'ils prennent des actions en conséquence,
 * comme l'envoi d'un email ou la mise à jour du statut du document.
 */
public class DocumentSavedEvent extends ApplicationEvent {

    // Document associé à l'événement
    private final Document document;

    /**
     * Crée un nouvel événement de sauvegarde de document.
     *
     * @param source   L'objet qui déclenche cet événement (généralement l'objet "this" de la classe appelante).
     * @param document Le document qui vient d'être sauvegardé. Ne peut pas être nul.
     * @throws IllegalArgumentException si le document est nul.
     */
    public DocumentSavedEvent(Object source, Document document) {
        super(source);
        if (document == null) {
            throw new IllegalArgumentException("Le document ne peut pas être nul.");
        }
        this.document = document;
    }

    /**
     * Retourne le document associé à cet événement.
     *
     * @return Le document sauvegardé.
     */
    public Document getDocument() {
        return document;
    }
}
