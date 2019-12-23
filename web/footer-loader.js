// effettua il caricamento del footer
$(function () {
    var $footer = $("#footer");
    $footer.load("footer.html");
    $footer.addClass("d-flex flex-column bg-light mt-5")
});