// Gestione della sessione utente lato client e caricamento dinamico
// dei contenuti della navbar

var autenticationChecker = new Vue({
   el: "#navbarSupportedContent",
   data: function() {
      var user = sessionStorage.getItem("username");
      var admin = JSON.parse(sessionStorage.getItem("admin"));
      var ospite = JSON.parse(sessionStorage.getItem("ospite"));
      console.log("user: " + user + " admin: " + admin + " ospite: " + ospite);
      if(user===null && admin===null && ospite===null) {
         alert("Non hai effettuato il login. Verrai reindirizzato a breve");
         window.location.replace("/progetto_ium_tweb2/loginPage.html");
      } else if(ospite===null || ospite) {
         var isRedirect = window.location.search.search("redirect=");
         var path;
         console.log(isRedirect);
         if(isRedirect===-1)
            path=window.location.pathname;
         else
            path = "/progetto_ium_tweb2/" + window.location.search.substr(isRedirect+ 9) + ".html";
         console.log("currentPage: " + path);
         switch (path) {
            case "/progetto_ium_tweb2/storicoGenerale.html":
            case "/progetto_ium_tweb2/gestioneDocenti.html":
            case "/progetto_ium_tweb2/gestioneCorsi.html":
            case "/progetto_ium_tweb2/gestioneStorico.html":
               alert("Sei ospite! Non hai i permessi per visualizzare le informazioni di questa pagina. Verrai reindirizzato alla homepage");
               window.location.replace("/progetto_ium_tweb2/homepage.html");
               break;
         }
      }
      else if (admin===null || !admin) {
         switch (window.location.pathname) {
            case "/progetto_ium_tweb2/storicoGenerale.html":
            case "/progetto_ium_tweb2/gestioneDocenti.html":
            case "/progetto_ium_tweb2/gestioneCorsi.html":
               alert("Non hai i permessi di admin per visualizzare le informazioni di questa pagina. Verrai reindirizzato alla homepage");
               window.location.replace("/progetto_ium_tweb2/homepage.html");
               break;
         }
      }
      return {username: user, admin:admin, ospite:ospite};
   },
   methods: {
      getNomeUtente() {
         return this.username || "ospite";
      },
   },
});