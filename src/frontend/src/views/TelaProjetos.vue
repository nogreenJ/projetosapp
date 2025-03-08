<template>
    <!--Listagem de Projetos-->
    <div class="mainDiv">
        <div v-if="!showEdtForm">
            <div class="row header">
                <div class="col-md-4 offset-md-4">
                    <h2 class="title">
                        Projetos 
                    </h2>
                </div>
                <div class="actionsTopBar col-md-4">
                    <Button title="Novo Projeto"
                        data-target="#modalProjeto" data-toggle="modal"
                        @click="newProjeto()">
                        <i class="fa fa-plus"></i>
                    </Button>
                </div>
            </div>
            <DataTable :value="projetoLista" tableStyle="" paginator :rows="12">
                <Column field="id" header="Código" style="width: 7%"></Column>
                <Column field="status" header="Status" style="width: 10%">
                    <template #body="slotProps">
                        <span class="cut-text">{{ renderStatusProjetoName(slotProps.data.status)}}</span>
                    </template>
                </Column>
                <Column field="titulo" header="Título" style="width: 13%">
                    <template #body="slotProps">
                        <span class="cut-text">{{ slotProps.data.titulo}}</span>
                    </template>
                </Column>
                <Column field="clienteParent" header="Cliente" style="width: 12%">
                    <template #body="slotProps">
                        <span class="cut-text">{{ 
                        slotProps.data.clienteParent ? slotProps.data.clienteParent.id + " - " + slotProps.data.clienteParent.nome : '-'
                        }}</span>
                    </template>
                </Column>
                <Column field="descricao" header="Descrição" style="width: 18%">
                    <template #body="slotProps">
                        <span class="cut-text">{{ slotProps.data.descricao}}</span>
                    </template>
                </Column>
                <Column field="dataCriacao" header="Data Criação" style="width: 9%">
                    <template #body="slotProps">
                        {{ slotProps.data.dataCriacao ? dateObjToBRDate(slotProps.data.dataCriacao) : '-' }}
                    </template>
                </Column>
                <Column field="dataPrevista" header="Data Prevista" style="width: 9%">
                    <template #body="slotProps">
                        {{ slotProps.data.dataPrevista ? dateObjToBRDate(slotProps.data.dataPrevista) : '-' }}
                    </template>
                </Column>
                <Column field="dataConclusao" header="Data Conclusão" style="width: 9%">
                    <template #body="slotProps">
                        {{ slotProps.data.dataConclusao ? dateObjToBRDate(slotProps.data.dataConclusao) : '-' }}
                    </template>
                </Column>
                <Column header="Ações"  style="width: 13%">
                    <template #body="slotProps">
                        <div>
                            <Button  
                                variant="text" :title="slotProps.data.status == 'EM_DESENVOLVIMENTO' ? 'Editar Projeto' : 'Visualizar Projeto'"
                                @click="editProjeto(slotProps.data)">
                                <i :class="slotProps.data.status == 'EM_DESENVOLVIMENTO' ? 'fa fa-pencil' : 'fa fa-eye'"></i>
                            </Button>
                            <Button  
                                v-if="slotProps.data.status == 'EM_DESENVOLVIMENTO'"
                                variant="text" title="Excluir Projeto"
                                @click="deleteProjeto(slotProps.data)">
                                <i class="fa fa-trash"></i>
                            </Button>
                            <Button  
                                v-if="slotProps.data.status == 'EM_DESENVOLVIMENTO'"
                                variant="text" title="Finalizar Projeto"
                                @click="finalizarProjeto(slotProps.data, true)">
                                <i class="fa fa-check"></i>
                            </Button>
                            <Button  
                                v-if="slotProps.data.status == 'EM_DESENVOLVIMENTO'"
                                variant="text" title="Cancelar Projeto"
                                @click="finalizarProjeto(slotProps.data, false)">
                                <i class="fa fa-times"></i>
                            </Button>
                        </div>
                    </template>
                </Column>
            </DataTable>
        </div>

        <!--Formulário de criação e edição de projetos-->
        <div v-if="showEdtForm" modal header="Editar Projeto" style="padding-top: 20px;">
            <div class="row header">
                <div class="col-md-4 offset-md-4">
                    <h2 class="title" v-if="projetoEdt.status == 'EM_DESENVOLVIMENTO' && isEditar">
                        Editar Projeto 
                    </h2>
                    <h2 class="title" v-if="projetoEdt.status == 'EM_DESENVOLVIMENTO' && !isEditar">
                        Cadastrar Projeto 
                    </h2>
                    <h2 class="title" v-if="projetoEdt.status != 'EM_DESENVOLVIMENTO'">
                        Visualizar Projeto 
                    </h2>
                </div>
                <div class="actionsTopBar col-md-4">
                    <Button type="button" label="Salvar" @click="saveProjeto" v-if="projetoEdt.status == 'EM_DESENVOLVIMENTO'">
                        <i class="fa fa-check"></i> Salvar
                    </Button>
                    <Button type="button" label="Cancelar" severity="secondary" @click="showEdtForm = false"><i class="fa fa-times"></i>Cancelar</Button>
                </div>
            </div>
            <Form>
                <Fieldset legend="Dados do Projeto" pt:content:class="flex justify-center">
                    <div class="row">
                        <div class="col-md-1">
                            <label for="codigo">Código</label>
                        </div>
                        <div class="col-md-2">
                            <InputText type="text" id="codigo" v-model="projetoEdt.id" class="form-control" placeholder="Código" readonly/>
                        </div>
                        <div class="col-md-1">
                            <label for="dataPrevista">Data Prevista</label>
                        </div>
                        <div class="col-md-2">
                            <DatePicker type="text" id="dataPrevista" v-model="projetoEdt.dataPrevista" fluid showIcon 
                            :readonly="projetoEdt.status != 'EM_DESENVOLVIMENTO'"
                            placeholder="Data Prevista" :invalid="!!errors.dataPrevista" dateFormat="dd/mm/yy"/>
                            <span v-if="errors.dataPrevista" class="errormsg">{{ errors.dataPrevista }}</span>
                        </div>
                        <div class="col-md-1" v-if="projetoEdt.status != 'EM_DESENVOLVIMENTO'">
                            <label for="dataConclusao">Data Conclusão</label>
                        </div>
                        <div class="col-md-2" v-if="projetoEdt.status != 'EM_DESENVOLVIMENTO'">
                            <DatePicker type="text" id="dataConclusao" v-model="projetoEdt.dataConclusao" fluid showIcon readonly 
                            placeholder="Data de Conclusão" :invalid="!!errors.dataConclusao" dateFormat="dd/mm/yy"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-1">
                            <label for="titulo">Título</label>
                        </div>
                        <div class="col-md-6">
                            <InputText type="text" id="titulo" v-model="projetoEdt.titulo" class="form-control" 
                            placeholder="Título" :invalid="!!errors.titulo" maxlength="40" :readonly="projetoEdt.status != 'EM_DESENVOLVIMENTO'"/>
                            <span v-if="errors.titulo" class="errormsg">{{ errors.titulo }}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-1">
                            <label for="clienteParent">Cliente</label>
                        </div>
                        <div class="col-md-6">
                            <ClienteSelect id="clienteParent" v-model="projetoEdt.clienteParent.id" :disabled="!!projetoEdt.id"/>
                            <span v-if="errors.clienteParent" class="errormsg">{{ errors.clienteParent }}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-1">
                            <label for="descricao">Descrição</label>
                        </div>
                        <div class="col-md-9">
                            <InputText type="text" id="descricao" v-model="projetoEdt.descricao" class="form-control" 
                            placeholder="Descrição" :invalid="!!errors.descricao" maxlength="30" :readonly="projetoEdt.status != 'EM_DESENVOLVIMENTO'"/>
                            <span v-if="errors.descricao" class="errormsg">{{ errors.descricao }}</span>
                        </div>
                    </div>
                </Fieldset>
            </Form>
        </div>
    </div>

    <!--Modal de confirmação de deleção de projetos-->
    <Dialog v-model:visible="showDeleteModal" modal header="Excluir Projeto" :style="{ width: '30rem', 'margin-top': '-300px'}">
        <div>
            Deseja realmente excluir o Projeto <b>{{projetoEdt.id}} - {{projetoEdt.titulo}}</b>?
        </div>
        <div class="modal-footer">
            <Button type="button" @click="confirmDelete()"><i class="fa fa-check"></i> Confirmar</Button>
            <Button type="button" severity="secondary" @click="showDeleteModal = false"><i class="fa fa-times"></i> Cancelar</Button>
        </div>
    </Dialog>

    <!--Modal de confirmação de finalização/cancelamento de projetos-->
    <Dialog v-model:visible="showFinalizarProjeto" modal header="Finalizar Projeto" :style="{ width: '30rem', 'margin-top': '-300px'}">
        <div>
            Deseja realmente <b>{{ isFinalizar ? "finalizar" : "cancelar" }}</b> o Projeto <b>{{projetoEdt.id}} - {{projetoEdt.titulo}}</b>?
        </div>
        <div class="modal-footer">
            <Button type="button" @click="confirmFinaliza()"><i class="fa fa-check"></i> Confirmar</Button>
            <Button type="button" severity="secondary" @click="showFinalizarProjeto = false"><i class="fa fa-times"></i> Cancelar</Button>
        </div>
    </Dialog>
</template>

<script>
import { ref, onMounted } from "vue";
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { useToast } from 'primevue/usetoast';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import { Form } from "@primevue/forms";
import { Fieldset, DatePicker } from "primevue";
import ClienteSelect from "@/components/ClienteSelect.vue";
import { dateObjToBRDate } from "@/assets/DateUtils";
import { renderStatusProjetoName } from "@/assets/Utils";

export default {
    components: {
        DataTable,
        Column,
        Button,
        Dialog,
        InputText,
        Fieldset,
        Form,
        DatePicker,
        ClienteSelect
    },
    methods: { dateObjToBRDate, renderStatusProjetoName },
    name: 'TelaProjetos',
    setup(){
        const defaultProjeto = {
            id: null,
            titulo: "",
            dataPrevista: "",
            descricao: "",
            status: "EM_DESENVOLVIMENTO",
            clienteParent: {id: ""},
        }

        let projetoLista = ref([]);
        let projetoEdt = ref(defaultProjeto);
        let isEditar = ref(true);
        let showEdtForm = ref(false);
        let showDeleteModal = ref(false);
        let showFinalizarProjeto = ref(false);
        //True se forfinalizar, false se for cancelar
        let isFinalizar = ref(false);

        const toast = useToast();

        const toastMsg = (params) =>{
            if(!params.status || !params.descricao){
                params.status = "error";
                params.descricao = "Erro.";
            } else {
                params.status = params.status.toLowerCase();
            }
            toast.add({
                severity: params.status == "warning" ? 'warn' : params.status,
                summary: params.descricao,
                life: 5000
            });
        }

        let errors = ref({
            titulo: "",
            descricao: "",
            clienteParent: "",
            dataPrevista: "",
        });

        const editProjeto = (projeto) => {
            showEdtForm.value = true;
            isEditar.value = true;
            cleanProjeto();
            if(projeto && projeto.id){
                fetch("/api/projeto/" + projeto.id)
                    .then((response) => response.json())
                    .then((data) => {
                        if(data.status == "SUCCESS"){
                            const obj = data.dataMap.obj;
                            obj.dataCriacao = dateObjToBRDate(obj.dataCriacao);
                            obj.dataPrevista = dateObjToBRDate(obj.dataPrevista);
                            obj.dataConclusao = dateObjToBRDate(obj.dataConclusao);
                            projetoEdt.value = obj;
                        } else {
                            toastMsg(data); 
                            showEdtForm.value = false;
                        }
                    });
            }
        };

        const newProjeto = () => {
            cleanProjeto();
            isEditar.value = false;
            showEdtForm.value = true;
        };

        const cleanProjeto = () =>{
            projetoEdt.value = defaultProjeto;
            errors.value = { titulo: "", descricao: "", clienteParent: "" };
        }

        const deleteProjeto = (projeto) => {
            projetoEdt.value = projeto;
            showDeleteModal.value = true;
        };

        // Função que é chamada quando o usuário confirma a deleção
        const confirmDelete = () => {
            if (projetoEdt.value && projetoEdt.value.id) {
                fetch("/api/projeto/" + projetoEdt.value.id, { method: "DELETE" })
                    .then((response) => response.json())
                    .then((data) => {
                        if (data.status == "SUCCESS") {
                            loadList();
                        }
                        toastMsg(data);
                    })
                    .catch((error) => console.error("Erro ao deletar Projeto: ", error));
                cleanProjeto();
                showDeleteModal.value = false;
            }
        };

        const finalizarProjeto = (projeto, finalCancel) => {
            projetoEdt.value = projeto;
            showFinalizarProjeto.value = true;
            isFinalizar.value = finalCancel;
        };

        // Função que é chamada quando o usuário confirma a finalização/deleção
        const confirmFinaliza = () => {
            if (projetoEdt.value && projetoEdt.value.id) {
                fetch((isFinalizar.value ? "/api/projeto/finalizar/" : "/api/projeto/cancelar/") + projetoEdt.value.id, { method: "POST" })
                    .then((response) => response.json())
                    .then((data) => {
                        if (data.status == "SUCCESS") {
                            loadList();
                        }
                        toastMsg(data);
                    })
                    .catch((error) => console.error("Erro ao finalizar Projeto: ", error));
                cleanProjeto();
                showFinalizarProjeto.value = false;
            }
        };

        const validateForm = () => {
            errors.value = { titulo: "", descricao: "", clienteParent: "" , dataPrevista: "" };

            if (!projetoEdt.value.titulo.trim()) {
                errors.value.titulo = "O titulo é obrigatório.";
            }
            if (!projetoEdt.value.descricao.trim()) {
                errors.value.descricao = "A descrição é obrigatória.";
            }
            if (!projetoEdt.value.clienteParent || !projetoEdt.value.clienteParent.id) {
                errors.value.clienteParent = "Informe o Cliente do Projeto.";
            }
            if (!projetoEdt.value.dataPrevista) {
                errors.value.dataPrevista = "Informe a Data Prevista.";
            } else {
                const currentDate = new Date();
                currentDate.setHours(0,0,0,0);
                if(typeof projetoEdt.value.dataPrevista == "string"){
                    var dividir = projetoEdt.value.dataPrevista.split("/");
                    var conquistar = new Date(dividir[2], dividir[1], dividir[0]);
                    projetoEdt.value.dataPrevista = conquistar;
                }
                if (projetoEdt.value.dataPrevista.getTime() < currentDate.getTime()) {
                    errors.value.dataPrevista = "A Data não pode ser anterior à data atual.";
                }
            }

            return !errors.value.titulo && !errors.value.descricao && !errors.value.clienteParent && !errors.value.dataPrevista;
        };

        const saveProjeto = () => {
            if (!validateForm()) return;
            projetoEdt.value.dataCriacao = "";
            
            fetch("/api/projeto", {
                method: isEditar.value ? "PUT" : "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(projetoEdt.value),
            })
            .then((response) => response.json())
            .then((data) => {
                if(data.status == "SUCCESS"){
                    showEdtForm.value = false;
                    loadList();
                }
                toastMsg(data);
            })
            .catch(error => console.error("Erro ao salvar Projeto: ", error));
        };

        const loadList = () => {
            fetch("/api/projeto")
                .then((response) => response.json())
                .then((data) => {
                    if(data.status == "SUCCESS"){
                        projetoLista.value = data.dataMap.list;
                    } else {
                        toastMsg(data);
                    }
                })
                .catch(error => console.error("Erro ao listar Projetos: ", error));
        }

        onMounted(() => {
            loadList();
        });

        return { projetoLista, isEditar, showEdtForm, showDeleteModal, projetoEdt, errors, showFinalizarProjeto, isFinalizar, 
            finalizarProjeto, confirmFinaliza, editProjeto, deleteProjeto, newProjeto, saveProjeto, loadList, confirmDelete };
    }
}
</script>

<style lang="scss">

    .mainDiv {
        padding: 0 25px;
        border-radius: 10px;
        height: 92.3vh;
        background-color: var(--p-zinc-900);
    }

    .title {
        text-align: center;
        font-size: 1.5rem;
        font-weight: bold;
        color: var(--p-datatable-header-cell-color);
        margin-bottom: 20px;
    }

    .header {
        padding-top: 20px;
    }

    .actionsTopBar {
        text-align: right;
    }

    .row {
        margin: -2px 0;
    }

    form{
        .row {
            margin: 3px 0;
        } 
        label {
            padding-top: 9px;
        } 
    }

    .modal-header {
        display: flex;
        flex-shrink: 0;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-end;
        margin-bottom: 20px;
    }

    .errormsg {
        color: red;
        font-size: 13px;
    }

    .cut-text { 
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
    }

</style>