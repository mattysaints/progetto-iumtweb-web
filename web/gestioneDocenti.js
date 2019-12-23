var listDoc = new Vue({
   el: '#listDoc',
   data: {
      docenti: [],
      areaAggiungi: false,
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
Vue.component('docente', {
   template:
      `
      <button class="btn btn-light btn-block btn-lg">
         <slot></slot> <span class="badge badge-primary rounded">n° corsi</span>
       </button>
      `
});
Vue.component('docente-corsi', {
   template:
      `
      <label>text</label> //todo da fare
      `
});