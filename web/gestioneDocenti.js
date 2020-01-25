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
        this.initLibraries();
    },
    methods: {
        initLibraries: function () {
            toastr.options.closeButton = true;
            toastr.options.positionClass = "toast-bottom-right";
            toastr.options.newestOnTop = false;
            var it = {
                OK: 'Ok',
                CONFIRM: 'Ok',
                CANCEL: 'Cancella'
            };
            bootbox.addLocale("it", it);
        },
        getDocenti: function () {
            var thiz = this;
            $.ajax({
                url: thiz.link,
                method: 'post',
                data: {
                    op: "visualizzare",
                },
                success: function (data) {
                    if (!data.successo)
                        toastr.error("Errore nel caricamento dei docenti", "Errore");
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
                                success: function (dati) {
                                    if (dati.successo) {
                                        corsi = dati.corsi;
                                    } else
                                        toastr.error("Errore nel caricamento degli insegnamenti", "Errore");
                                },
                                async: false,
                            }); //end ajax
                            thiz.docenti[i].corsiInsegnati = corsi;
                        } //end for
                        console.log(this.docenti);
                    } //end else
                } //end success
            }); //end ajax
        },
        eliminaDocente: function (docente, index) {
            var thiz = this;
            var message = "";
            if (this.docenti[index].corsiInsegnati.length > 0)
                message += "<p class='text-warning font-weight-bold text-lg-left'>Attenzione</p>Il docente ha dei corsi associati. ";
            message += "Sei sicuro di voler eliminare <span class='font-weight-bold'>" + docente.cognome + " " + docente.nome + "</span>?";
            bootbox.confirm({
                message: message,
                locale: "it",
                callback: function (confirm) {
                    if (confirm) {
                        $.ajax({
                            method: 'post',
                            url: thiz.link,
                            data: {
                                op: "eliminare",
                                docente: JSON.stringify(docente),
                            },
                            success: function (data) {
                                if (data.successo) {
                                    thiz.docenti.splice(index, 1);
                                    toastr.success("Docente <span class='font-weight-bold'>" + docente.cognome + " "
                                        + docente.nome + "</span> eliminato correttamente", "Successo");
                                } else
                                    toastr.error("Errore nell'eliminazione del docente", "Errore");
                            },
                            async: false,
                        });
                    }
                }
            });
        },
        aggiungiDocente: function () {
            var $nome = $("#nome");
            var $cognnome = $("#cognome");
            if (this.docenti.filter(d =>
                d.docente.nome === this.docente.nome && d.docente.cognome === this.docente.cognome)
                .length > 0) {
                toastr.error("Il docente <span class='font-weight-bold'>" + this.docente.nome + " " + this.docente.cognome + "</span> è gia presente", "Errore");
                return;
            }
            var thiz = this;
            $.ajax({
                method: "post",
                url: thiz.link,
                data: {
                    op: "inserire",
                    docente: JSON.stringify(thiz.docente),
                },
                success: function (data) {
                    if (data.successo) {
                        thiz.docenti.push({
                            docente: {
                                cognome: thiz.docente.cognome,
                                nome: thiz.docente.nome
                            },
                            corsiInsegnati: []
                        });
                        toastr.success("Docente <span class='font-weight-bold'>" + thiz.docente.nome + " " + thiz.docente.cognome + "</span> inserito correttamente", "Successo");
                    } else {
                        toastr.error("Errore nell'inserimento del docente", "Errore");
                    }
                },
                async: true,
            });
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
                    success: function (data) {
                        if (data.successo) {
                            thiz.docenti[index].corsiInsegnati.push({titolo: corso});
                            toastr.success("Corso inserito: <span class='font-weight-bold'>" + docente.nome + " "
                                + docente.cognome + "</span> dà ripetizioni di <span class='font-weight-bold'>"
                                + corso + "</span>", "Successo")
                        } else
                            toastr.error("Errore nell'inserimento del corso", "Errore");
                    },
                    async: true
                });
            } else {
                toastr.error("Scegliere un corso da inserire", "Errore");
            }
        },
        eliminaCorso: function (docente, indexD, corso, indexC) {
            var thiz = this;
            bootbox.confirm({
                message: "<span class='font-weight-bold'>" + docente.cognome + " " + docente.nome
                    + "</span> non darà più ripetizioni di <span class='font-weight-bold'>"
                    + corso.titolo + "</span>. Confermare?",
                locale: "it",
                callback: function (confirm) {
                    if (confirm) {
                        $.ajax({
                            url: thiz.linkCorsi,
                            method: 'post',
                            data: {
                                op: "eliminare",
                                docente: JSON.stringify(docente),
                                corso: JSON.stringify(corso),
                            },
                            success: function (data) {
                                if (data.successo) {
                                    thiz.docenti[indexD].corsiInsegnati.splice(indexC, 1);
                                    toastr.success("<span class='font-weight-bold'>" + docente.cognome + " " + docente.nome
                                        + "</span> non dà più ripetizioni di <span class='font-weight-bold'>" + corso.titolo
                                        + "</span>", "Successo")
                                } else
                                    toastr.error("Errore nell'eliminazione del corso", "Errore");
                            },
                            async: false,
                        })
                    }
                }
            });
        }
    },
});

Vue.component("box-docente", {
    template:
        `
      <div class="card">
         <docente class="card-header align-items-start text-left pl-lg-5" data-toggle="collapse" :data-target="'#collapseDoc' + i" v-bind:docente="docente" v-bind:indexDoc="i" v-bind:corsiInsegnati="corsiInsegnati">
         </docente>
         <insegnamenti class="collapse" :id="'collapseDoc' + i" data-parent="#lista" v-bind:docente="docente" v-bind:corsi.sync="corsiInsegnati" :indexDoc="i">
         </insegnamenti>
      </div>
   `,
    props: ['docente', 'i', 'corsiInsegnati'],
});

Vue.component('docente', {
    template:
        `
      <button class="btn btn-light" >
         <div class="row">
            <div class="col">
                {{docente.cognome}} {{docente.nome}}
                <span class="badge badge-primary rounded" data-toggle="tooltip" title="n° di corsi insegnati">{{corsiInsegnati.length}}</span>
            </div> 
            <div class="col text-center">
                <button class="btn btn-danger btn-sm collapse" :id="'collapseDoc' + indexDoc" data-parent="#lista" @click="$root.eliminaDocente(docente,indexDoc)"><i class="fas fa-times"></i> Elimina
                </button>
            </div>
         </div>
      </button>
      `,
    props: ['docente', 'indexDoc', 'corsiInsegnati'],
});

Vue.component('insegnamenti', {
    template:
        `<div id="insegnamenti">
              <div class="card-body pb-0"> 
                  <p class="text-primary">Nuovo insegnamento:</p>
                  <div class="mx-auto d-flex d-inline px-2">
                     <select class="ml-2 form-control" aria-required="true" v-model="corso" >
                        <option value="" hidden disabled selected>Scegli corso</option>
                        <option v-for="(cor, indexCor) in corsiAggiungibili" :value="cor">{{cor}}</option>
                     </select>
                     <button title="Inserisci un nuovo insegnamento" class="btn btn-primary ml-5 w-auto"
                     @click="$root.aggiungiCorso(docente, indexDoc, corso)">Inserisci</button>    
                  </div>
            </div>
            <hr>
            <div class="card-body pt-0">
                <p class="text-secondary mb-3">Lista corsi insegnati: </p>
                <p v-if="corsi.length === 0" class="border-top pt-2">Nessun corso da visualizzare</p>
                <table v-else class="table table-hover table-sm">
                    <thead class="thead-light">
                        <tr>
                            <!-- <th class="text-center" scope="col">#</th> -->
                            <th scope="col" class="pl-lg-3">Corso</th>
                            <th class="text-center" scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(c, indexCor) in corsi" :key="indexCor">
                            <!-- <td class="col col-auto text-center">{{indexCor+1}}</td> -->
                            <td class="col col-auto pl-3">{{c.titolo}}</td>
                            <td class="col col-auto text-center">
                                <button v-on:click="$root.eliminaCorso(docente, indexDoc, c, indexCor)" class="btn btn-danger btn-sm" title="Cancella il corso corrispondente"><i class="fas fa-times"></i></button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
      `,
    props: ['corsi', 'docente', 'indexDoc'],
    data: function () {
        return {
            link: "/progetto_ium_tweb2/GestioneInsegnamenti",
            corso: "",
        };
    },
    computed: {
        corsiAggiungibili: function () { //array dei corsi che possono essere aggiunti
            var tuttiCorsi = [];
            var thiz = this;
            $.ajax({
                method: "GET",
                url: "/progetto_ium_tweb2/GestioneCorsi",
                data: {
                    "op": "visualizzare",
                },
                success: function (data) {
                    if (data.result === "success")
                        tuttiCorsi = data.data.map(x => x.titolo);
                },
                async: false,
                complete: function () { // reset default
                    thiz.corso = "";
                    $("select").val("").change()
                }
            });
            var corsiInseg = this.corsi.map(x => x.titolo);
            return tuttiCorsi.filter(x => !corsiInseg.includes(x));
        }
    }
});
