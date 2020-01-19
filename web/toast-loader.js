/**
 * Permette di inserire dei toast, per evitare di usare alert, più invasivi
 * Passi da seguire per l'uso:
 * - includere il seguente script
 * - richiamare la funzione "makeToast()" quando serve
 */
$(document).ready(function () {
    var html = "<div aria-live='polite' aria-atomic='true' class='fixed-top mt-lg-5 w-100 d-flex flex-column p-4'>" +
        "    <div class='toast ml-auto' id='toast' data-delay='15000' role='alert'>" +
        "        <div class='toast-header'>" +
        "            <strong id='toast-title' class='mr-auto'></strong>" +
        "            <small>Adesso</small>" +
        "            <button type='button' class='ml-2 mb-1 close' data-dismiss='toast' aria-label='Chiudi'>" +
        "                <span aria-hidden='true'>&times;</span>" +
        "            </button>" +
        "        </div>" +
        "        <div id='toast-body' class='toast-body'></div>" +
        "    </div>" +
        "</div>";
    $(html).appendTo("body");
});

/**
 * Crea il toast per notifica
 *
 * @param classeTipo: classe (bootstrap) del testo che compone il titolo
 * @param titolo: testo che formerà il titolo
 * @param testo: corpo del toast
 */
var makeToast = function (classeTipo, titolo, testo) {
    var $titolo = $("#toast-title");
    $titolo.text(titolo);
    $titolo.removeClass("text-success text-light text-dark text-warning text-primary text-secondary text-info text-danger");
    $titolo.addClass(classeTipo);
    $("#toast-body").text(testo);
    $("#toast").toast("show");
};