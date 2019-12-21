var listDoc = new Vue({
    el: '#listDoc',
    data: {
        docenti: []
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
/*
Vue.component("box-docente", {
    template:
        `<div>
            <li class="list-group-item">
                {{doc.cognome}} {{doc.nome}} <span class="badge badge-primary rounded">n° corsi</span>
            </li>
            <list-corsi></list-corsi>
         </div>`,
    props: ['doc'],
});
Vue.component("list-corsi", {
    template:
        `<ol>
            <li class="list-group-item" v-for="n in 4">
               Cor{{n}}
            </li>
        </ol>`,
});
*/