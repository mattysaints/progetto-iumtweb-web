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
            self.docenti = data;
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
      <docente-corsi class="card-body collapse" :id="'collapseDoc' + i" data-parent="#accordion" >
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
         success: function (listCorsi) {
            if (listCorsi instanceof Boolean && !listCorsi)
               window.alert("ERRORE");
            else {
               ret.corsiInsegnati = listCorsi;
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
   //todo da fare
   template:
      `
      <label>text</label>
      `
});