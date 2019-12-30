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
$( document ).ready(function () {
    var $navbar = $("#navbar");
    $navbar.load("navbar.html", () => {
        var idActivePage = $navbar.attr("id-active-page");
        $navbar.addClass("fixed-top");
        $(idActivePage).addClass("active") //dava errore con # davanti
    });
    $("body").addClass("pt-5");
});

