var listDoc = new Vue({
   el: '#listDoc',
   data: {
      docenti: [],
      nuovoDocente: {},
   },
   mounted() {this.getDocenti()},
   methods: {
      getDocenti: function () {
         var self = this;
         $.get("/progetto_ium_tweb2/GestioneDocenti", {op: "visualizzare"}, function(data) {
            if(!data.successo)
               window.alert("Errore nel caricamento dei docenti");
            else
               self.docenti = data.docenti;
         });
      }
   }
});
Vue.component("box-docente", {
   template:
      `
      <div>
         <docente class="card-header align-items-start m-2">
             {{docente.cognome}} {{docente.nome}} <span class="badge badge-primary rounded">{{corsiInsegnati.length}}</span>
         </docente>
         <docente-corsi class="card-body collapse" :id="'collapseDoc' + i" data-parent="#accordion" v-bind:docente="docente" v-bind:corsi="corsiInsegnati">
         </docente-corsi>
      </div>
   `,
   props: ['docente', 'i'],
   data: function() {
      var ret = {
         corsiInsegnati: [],
      };
      var self = this;
      $.ajax({
         type: 'POST',
         url: "/progetto_ium_tweb2/GestioneInsegnamenti",
         data: {
            "op": "visualizzare",
            "docente": JSON.stringify(self.docente),
         },
         success: function (data) {
            if (!data.successo)
               window.alert("Errore nella ricezione degli insegnamenti");
            else {
               ret.corsiInsegnati = data.corsi;
            }
         },
         async: false,
      });
      return ret;
   },
});
Vue.component('docente', {
   template:
      `
      <button class="btn btn-light btn-block btn-lg">
         <slot></slot>
       </button>
      `
});
Vue.component('docente-corsi', {
   template:
      `
      <div id="insegnamenti">
         <div class="container-fluid card">
            <form action="javascript:insegnamenti.aggiungiCorso()">
                <div class="mx-auto text-center">
                  <p class="text-primary">Nuovo corso:<input class="ml-2" aria-required="true" v-model="corso" type="text" placeholder="Corso" required/> <button title="Inserisci un nuovo corso" type="submit" class="btn btn-primary ml-5 w-auto">Inserisci</button></p>
               </div>
            </form>
         </div>

         <div class="container-fluid card">
            <p class="text-primary my-3">Lista corsi insegnati: </p>
            <p v-if="corsi.length === 0" class="border-top pt-2">Nessun corso da visualizzare</p>
            <table v-else class="table table-striped">
               <thead>
                  <tr>
                      <th scope="col">#</th>
                      <th scope="col">Corso</th>
                      <th scope="col"></th>
                  </tr>
               </thead>
               <tbody>
               <tr v-for="(c, index) in corsi" :key="index">
                   <td class="col-md-1">{{index+1}}</td>
                   <td>{{c.titolo}}</td>
                   <td class="align-content-center col-md-3">
                       <button v-on:click="eliminaCorso(c.titolo, index)" class="btn btn-secondary btn-sm" title="Cancella il corso corrispondente"><i class="fas fa-times"></i></button>
                   </td>
               </tr>
               </tbody>
            </table>
         </div>
      </div>
      `,
   props: ['corsi', 'docente'],
   data: () => {
      var data = {
         link: "/progetto_ium_tweb2/GestioneInsegnamenti",
         corso: "",
      };
      return data;
   },
   methods: {
      checkAutentication: function () {
         this.username = sessionStorage.getItem("username");
         this.admin = sessionStorage.getItem("admin") === "true";

         if (this.username === null || this.admin === null) {
            window.location.replace("/progetto_ium_tweb2/loginPage.html");
            return false;
         } else
            return true;
      },
      aggiungiCorso: function () {
         var self = this;
         $.post(this.link, {
            op: "inserire",
            corso: this.corso,
            docente: this.docente,
         }, data => {
            if (data.successo)
               self.corsi.push({titolo: self.corso});
            else
               window.alert("Errore nell'inserimento del corso");
         })
      },
      eliminaCorso: function (corso, index) {
         var self = this;
         $.post(this.link, {
            op: "eliminare",
            docente: this.docente,
            corso: corso,
         }, data => {
            if (data.successo)
               self.corsi.splice(index, 1);
            else
               window.alert("Errore nell'eliminazione del corso");
         })
      }
   },
});