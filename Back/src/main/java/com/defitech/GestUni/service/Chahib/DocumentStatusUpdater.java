package com.defitech.GestUni.service.Chahib;

import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.models.Chahib.StatutDocument;
import org.springframework.stereotype.Component;

@Component
public class DocumentStatusUpdater {
    public void updateStatus(Document document, StatutDocument statut) {
        document.setStatut(statut);
}
}
