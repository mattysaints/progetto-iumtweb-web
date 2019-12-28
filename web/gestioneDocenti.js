var listDoc = new Vue({
   el: '#listDoc',
   data: {
      link: "/progetto_ium_tweb2/GestioneDocenti",
      linkCorsi: "/progetto_ium_tweb2/GestioneInsegnamenti",
      docenti: [], //contiene docente e corsiInsegnati
      docente: { //per aggiungere docenti
         cognome: "",
         nome: ""
      },
   },
   mounted() {
      this.getDocenti();
   },
   methods: {
      getDocenti: function () {
         var thiz = this;
         $.ajax({
            url: thiz.link,
            method: 'post',
            data: {
               op: "visualizzare",
            },
            success: function(data) {
               if (!data.successo)
                  window.alert("Errore nel caricamento dei docenti");
               else {
                  for (var i = 0; i < data.docenti.length; i++) {
                     thiz.docenti.push({
                        docente: data.docenti[i],
                        corsiInsegnati: []
                     });
                     var corsi = [];
                     $.ajax({
                        method: 'post',
                        url: thiz.linkCorsi,
                        data: {
                           op: "visualizzare",
                           docente: JSON.stringify(thiz.docenti[i].docente),
                        },
                        success: function(dati) {
                           if (dati.successo) {
                              corsi = dati.corsi;
                           } else
                              window.alert("Errore nel caricamento degli insegnamenti");
                        },
                        async: false,
                     }); //end ajax
                     thiz.docenti[i].corsiInsegnati = corsi;
                  } //end for
               } //end else
            } //end success
         }); //end ajax
      },
      eliminaDocente: function(docente, index) {
         var thiz = this;
         $.ajax({
            method: 'post',
            url: thiz.link,
            data: {
               op: "eliminare",
               docente: JSON.stringify(docente),
            },
            success: function (data) {
               if (data.successo)
                  thiz.docenti.splice(index, 1);
               else
                  alert("Errore nell'eliminazione del docente");
            },
            async: false,
         });
      },
      aggiungiDocente: function() {
         var thiz = this;
         $.ajax({
            method: "post",
            url: thiz.link,
            data: {
               op: "inserire",
               docente: JSON.stringify(thiz.docente),
            },
            success: function (data) {
               if (data.successo)
                  thiz.docenti.push({
                     docente: {cognome: thiz.docente.cognome, nome: thiz.docente.nome},
                     corsiInsegnati: []
                  });
               else
                  alert("Errore nell'inserimento del docente");
            },
            async: false,
         })
      },
      aggiungiCorso: function (docente, index, corso) {
         var thiz = this;
         if (corso !== "") {
            $.ajax({
               method: 'post',
               url: thiz.linkCorsi,
               data: {
                  op: "inserire",
                  corso: JSON.stringify({titolo: corso}),
                  docente: JSON.stringify(docente),
               },
               success: function(data) {
                  if (data.successo)
                     thiz.docenti[index].corsiInsegnati.push({titolo: corso});
                  else
                     window.alert("Errore nell'inserimento del corso");
               },
               async: false,
            });
         } else
            window.alert("Errore nell'inserimento del corso");
      },
      eliminaCorso: function (docente, indexD, corso, indexC) {
         var thiz = this;
         $.ajax({
            url: thiz.linkCorsi,
            method: 'post',
            data: {
               op: "eliminare",
               docente: JSON.stringify(docente),
               corso: JSON.stringify(corso),
            },
            success: function(data) {
               if (data.successo)
                  thiz.docenti[indexD].corsiInsegnati.splice(indexC, 1);
               else
                  window.alert("Errore nell'eliminazione del corso");
            },
            async: false,
         })
      }
   },
});

Vue.component("box-docente", {
   template:
      `
      <div class="card">
         <docente class="card-header align-items-start text-left pl-lg-5" data-toggle="collapse" :data-target="'#collapseDoc' + i">
             {{docente.cognome}} {{docente.nome}} <span class="badge badge-primary rounded" data-toggle="tooltip" title="n° di corsi insegnati">{{corsiInsegnati.length}}</span> <slot></slot>
         </docente>
         <docente-corsi class="collapse" :id="'collapseDoc' + i" data-parent="#listDoc" v-bind:docente="docente" v-bind:corsi.sync="corsiInsegnati" :i="i">
         </docente-corsi>
      </div>
   `,
   props: ['docente', 'i', 'corsiInsegnati'],
});

Vue.component('docente', {
   template:
      `
      <button class="btn btn-light btn-block btn-lg" >
         <slot></slot>
       </button>
      `
});

Vue.component('docente-corsi', {
   template:
      `
      <div id="insegnamenti" class="card-body" >
         <div class="container-fluid">
             <div class="mx-auto">
               <p class="text-primary">Nuovo insegnamento:</p>
               <div class="mx-auto d-flex d-inline">
                  <select class="ml-2 form-control" aria-required="true" v-model="corso" >
                     <option value="" selected disabled hidden>Scegli corso</option>
                     <option v-for="(cor, index) in corsiAggiungibili" :value="cor" :selected="index===1">{{cor}}</option>
                  </select>
                  <button title="Inserisci un nuovo insegnamento" class="btn btn-primary ml-5 w-auto" @click="$root.aggiungiCorso(docente, i, corso)">Inserisci</button>    
               </div>
            </div>
         </div>

         <div class="container-fluid card no-collapsible mt-3">
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
                       <button v-on:click="$root.eliminaCorso(docente, i, c, index)" class="btn btn-secondary btn-sm" title="Cancella il corso corrispondente"><i class="fas fa-times"></i></button>
                   </td>
               </tr>
               </tbody>
            </table>
         </div>
         <hr class="my-2">
         <div class="text-right">
                <button class="btn btn-danger" @click="$root.eliminaDocente(docente,i)"><i class="fas fa-times"></i> Elimina docente</button>
         </div>
      </div>
      `,
   props: ['corsi', 'docente', 'i'],
   data: function() {
      return {
         link: "/progetto_ium_tweb2/GestioneInsegnamenti",
         corso: "",
      };
   },
   computed: {
      corsiAggiungibili: function () { //array dei corsi che possono essere aggiunti
         var tuttiCorsi=[];
         $.ajax({
            method: "GET",
            url: "/progetto_ium_tweb2/GestioneCorsi",
            data: {
               "op": "visualizzare",
            },
            success: function(data) {
               if(data.result==="success")
                  tuttiCorsi = data.data.map(x => x.titolo);
            },
            async: false,
         });
         var corsiInseg = this.corsi.map(x => x.titolo);
         return tuttiCorsi.filter(x => !corsiInseg.includes(x));
      }
   }
});
