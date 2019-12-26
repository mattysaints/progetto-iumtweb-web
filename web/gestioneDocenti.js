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
      <div class="card">
         <docente class="card-header align-items-start" data-toggle="collapse" :data-target="'#collapseDoc' + i">
             {{docente.cognome}} {{docente.nome}} <span class="badge badge-primary rounded">{{corsiInsegnati.length}}</span>
         </docente>
         <docente-corsi class="collapse" :id="'collapseDoc' + i" data-parent="#accordion" v-bind:docente="docente" v-bind:corsi="corsiInsegnati">
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
      <div id="insegnamenti" class="card-body">
         <div class="container-fluid">
             <div class="mx-auto text-center">
               <p class="text-primary">Nuovo insegnamento:
                  <select class="ml-2" aria-required="true" v-model="corso" >
                     <option v-for="(cor, index) in corsiAggiungibili" :value="cor" :selected="index===1">{{cor}}</option>
                  </select>
                  <button title="Inserisci un nuovo insegnamento" class="btn btn-primary ml-5 w-auto" @click="aggiungiCorso()">Inserisci</button>
               </p>
            </div>
         </div>

         <div class="container-fluid card no-collapsible">
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
                       <button v-on:click="eliminaCorso(c, index)" class="btn btn-secondary btn-sm" title="Cancella il corso corrispondente"><i class="fas fa-times"></i></button>
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
         if(this.corso!=="") {
            $.post(this.link, {
               op: "inserire",
               corso: JSON.stringify({titolo: this.corso}),
               docente: JSON.stringify(this.docente),
            }, data => {
               if (data.successo)
                  self.corsi.push({titolo: self.corso});
               else
                  window.alert("Errore nell'inserimento del corso");
            });
         }
         else
            window.alert("Errore nell'inserimento del corso");
      },
      eliminaCorso: function (corso, index) {
         var self = this;
         $.post(this.link, {
            op: "eliminare",
            docente: JSON.stringify(this.docente),
            corso: JSON.stringify(corso),
         }, data => {
            if (data.successo)
               self.corsi.splice(index, 1);
            else
               window.alert("Errore nell'eliminazione del corso");
         })
      }
   },
   computed: {
      corsiAggiungibili: function () { //array dei corsi che possono essere aggiunti
         //getter
         var tuttiCorsi=[];
         $.ajax({
            method: "GET",
            url: "/progetto_ium_tweb2/GestioneCorsi",
            data: {
               "op": "visualizzare",
            },
            success: (data) => {
               if(data.result==="success")
                  tuttiCorsi = data.data.map(x => x.titolo);
            },
            async: false,
         });
         var corsiInseg = this.corsi.map(x => x.titolo);
         let differenza= tuttiCorsi.filter(x => !corsiInseg.includes(x));
         return differenza;

      }
   }
});