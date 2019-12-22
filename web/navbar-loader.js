/**
 * Effettua il caricamento della navbar
 *
 * Uso:
 * <div id="navbar" id-active-page="id_della_pagina_corrente"></div>
 *
 * ID pagine: inseriti per ora
 * - home
 * - storico
 * - prenota
 */
$(function () {
    $("#navbar").load("navbar.html", () => {
        var idActivePage = $("#navbar").attr("id-active-page");
        $("#" + idActivePage + "").addClass("active")
    })
});