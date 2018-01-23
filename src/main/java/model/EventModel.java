package model;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EventModel extends Observable{

        private class Settori {
            private String nome;
            private int prezzo;
            private boolean riduzione;
        }


        List<Settori> listaSettori;
        private String nomeEvento;
        private boolean attivo;
        private String locandina;
        private Date dataInizio;
        private Date dataFine;
        private String descrizione;
        private String nomeLocation;
        private int maxVisitatori;
        private double riduzioneAnziani;
        private double riduzioneBambini;
        private double riduzioneStudenti;




        public List<Settori> getListaSettori() {
            return listaSettori;
        }

        public void setListaSettori(List<Settori> listaSettori) {
            this.listaSettori = listaSettori;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public boolean isAttivo() {
            return attivo;
        }

        public void setAttivo(boolean attivo) {
            this.attivo = attivo;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public String getLocandina() {
            return locandina;
        }

        public void setLocandina(String locandina) {
            this.locandina = locandina;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public String getNomeEvento() {
            return nomeEvento;
        }

        public void setNomeEvento(String nomeEvento) {
            this.nomeEvento = nomeEvento;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }


        public Date getDataInizio() {
            return dataInizio;
        }

        public void setDataInizio(Date dataInizio) {
            this.dataInizio = dataInizio;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public Date getDataFine() {
            return dataFine;
        }

        public void setDataFine(Date dataFine) {
            this.dataFine = dataFine;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public String getDescrizione() {
            return descrizione;
        }

        public void setDescrizione(String descrizione) {
            this.descrizione = descrizione;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public String getNomeLocation() {
            return nomeLocation;
        }

        public void setNomeLocation(String nomeLocation) {
            this.nomeLocation = nomeLocation;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public int getMaxVisitatori() {
            return maxVisitatori;
        }

        public void setMaxVisitatori(int maxVisitatori) {
            this.maxVisitatori = maxVisitatori;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public double getRiduzioneAnziani() {
            return riduzioneAnziani;
        }

        public void setRiduzioneAnziani(double riduzioneAnziani) {
            this.riduzioneAnziani = riduzioneAnziani;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public double getRiduzioneBambini() {
            return riduzioneBambini;
        }

        public void setRiduzioneBambini(double riduzioneBambini) {
            this.riduzioneBambini = riduzioneBambini;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }

        public double getRiduzioneStudenti() {
            return riduzioneStudenti;
        }

        public void setRiduzioneStudenti(double riduzioneStudenti) {
            this.riduzioneStudenti = riduzioneStudenti;
            setChanged(); //attiva il flag per gli observer
            notifyObservers(); //notifica gli observer
        }
    }