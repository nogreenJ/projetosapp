<template>
    <!--Listagem de Atividades-->
    <div class="miniDiv">
        <Fieldset pt:content:class="flex justify-center">
            <div class="row header">
                <div class="col-md-4 offset-md-4">
                    <h2 class="title">
                        Atividades do Projeto
                    </h2>
                </div>
                <div class="actionsTopBar col-md-4">
                    <Button title="Nova Atividade" v-if="projeto.status == 'EM_DESENVOLVIMENTO'"
                        data-target="#modalAtividade" data-toggle="modal"
                        @click="newAtividade()">
                        <i class="fa fa-plus"></i>
                    </Button>
                </div>
            </div>
            <DataTable :value="projeto.atividadeList" tableStyle="" paginator :rows="5" sortField="id" :sortOrder="-1">
                <Column field="id" header="Código" style="width: 7%"></Column>
                <Column field="status" header="Status" style="width: 10%">
                    <template #body="slotProps">
                        <span class="cut-text">{{ renderStatusAtividadeName(slotProps.data.status)}}</span>
                    </template>
                </Column>
                <Column field="titulo" header="Título" style="width: 13%">
                    <template #body="slotProps">
                        <span class="cut-text">{{ slotProps.data.titulo}}</span>
                    </template>
                </Column>
                <Column field="descricao" header="Descrição" style="width: 21%">
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
                <Column field="dataInicio" header="Data Início" style="width: 9%">
                    <template #body="slotProps">
                        {{ slotProps.data.dataInicio ? dateObjToBRDate(slotProps.data.dataInicio) : '-' }}
                    </template>
                </Column>
                <Column field="dataConclusao" header="Data Final" style="width: 9%">
                    <template #body="slotProps">
                        {{ slotProps.data.dataConclusao ? dateObjToBRDate(slotProps.data.dataConclusao) : '-' }}
                    </template>
                </Column>
                <Column header="Ações" style="width: 13%">
                    <template #body="slotProps">
                        <div v-if="projeto.status == 'EM_DESENVOLVIMENTO'">
                            <Button  
                                v-if="slotProps.data.status == 'NOVO' && !!projeto.id"
                                variant="text" title="Iniciar Atividade"
                                @click="iniciarAtividade(slotProps.data)">
                                <i class="fa fa-play"></i>
                            </Button>
                            <Button  
                                variant="text" v-if="slotProps.data.status == 'NOVO' || slotProps.data.status == 'EM_DESENVOLVIMENTO'" title="Editar Atividade"
                                @click="editAtividade(slotProps.data)">
                                <i class="fa fa-pencil"></i>
                            </Button>
                            <Button  
                                v-if="slotProps.data.status == 'NOVO'"
                                variant="text" title="Excluir Atividade"
                                @click="deleteAtividade(slotProps.data)">
                                <i class="fa fa-trash"></i>
                            </Button>
                            <Button  
                                v-if="slotProps.data.status == 'EM_DESENVOLVIMENTO' && !!projeto.id"
                                variant="text" title="Finalizar Atividade"
                                @click="finalizarAtividade(slotProps.data, true)">
                                <i class="fa fa-check"></i>
                            </Button>
                            <Button  
                                v-if="slotProps.data.status == 'NOVO' && !!projeto.id"
                                variant="text" title="Cancelar Atividade"
                                @click="finalizarAtividade(slotProps.data, false)">
                                <i class="fa fa-times"></i>
                            </Button>
                        </div>
                    </template>
                </Column>
            </DataTable>
        </Fieldset>
    </div>

    <!--Formulário de criação e edição de atividades-->
    <Dialog v-model:visible="showEdtModal" modal header="Atividade" :style="{ width: '80rem', 'margin-top': '-300px'}" :draggable="false">
        <form @submit.prevent="saveAtividade">
            <div class="row">
                <div class="col-md-1">
                    <label for="codigo">Código</label>
                </div>
                <div class="col-md-2">
                    <InputText type="text" id="codigo" v-model="atividadeEdt.id" class="form-control" placeholder="Código" readonly/>
                </div>
                <div class="col-md-1 pr-0">
                    <label for="dataPrevista">Dt. Prevista</label>
                </div>
                <div class="col-md-2">
                    <DatePicker type="text" id="dataPrevista" v-model="atividadeEdt.dataPrevista" fluid showIcon :readonly="!isAtividadeEditavel()"
                    placeholder="Data Prevista" :invalid="!!errors.dataPrevista" dateFormat="dd/mm/yy"/>
                    <span v-if="errors.dataPrevista" class="errormsg">{{ errors.dataPrevista }}</span>
                </div>
            </div>
            <div class="row">
                <div class="col-md-1">
                    <label for="titulo">Título</label>
                </div>
                <div class="col-md-6">
                    <InputText type="text" id="titulo" v-model="atividadeEdt.titulo" class="form-control" 
                    placeholder="Título" :invalid="!!errors.titulo" maxlength="40" :readonly="!isAtividadeEditavel()"/>
                    <span v-if="errors.titulo" class="errormsg">{{ errors.titulo }}</span>
                </div>
            </div>
            <div class="row">
                <div class="col-md-1">
                    <label for="descricao">Descrição</label>
                </div>
                <div class="col-md-9">
                    <InputText type="text" id="descricao" v-model="atividadeEdt.descricao" class="form-control" 
                    placeholder="Descrição" :invalid="!!errors.descricao" maxlength="30" :readonly="!isAtividadeEditavel()"/>
                    <span v-if="errors.descricao" class="errormsg">{{ errors.descricao }}</span>
                </div>
            </div>
            <div class="modal-footer">
                <Button type="submit" label="Salvar"><i class="fa fa-check" v-if="isAtividadeEditavel()"></i> Salvar</Button>
                <Button type="button" label="Cancelar" severity="secondary" @click="showEdtModal = false"><i class="fa fa-times"></i>Cancelar</Button>
            </div>
        </form>
    </Dialog>

    <!--Modal de confirmação de deleção de atividades-->
    <Dialog v-model:visible="showDeleteModal" modal header="Excluir Atividade" :style="{ width: '30rem', 'margin-top': '-300px'}" :draggable="false">
        <div>
            Deseja realmente excluir a Atividade<b>{{atividadeEdt.id ? " " + atividadeEdt.id + " - " + atividadeEdt.titulo : ""}}</b>?
        </div>
        <div class="modal-footer">
            <Button type="button" @click="confirmDelete()"><i class="fa fa-check"></i> Confirmar</Button>
            <Button type="button" severity="secondary" @click="showDeleteModal = false"><i class="fa fa-times"></i> Cancelar</Button>
        </div>
    </Dialog>

    <!--Modal de confirmação de finalização/cancelamento de atividades-->
    <Dialog v-model:visible="showFinalizarAtividade" modal header="Finalizar Atividade" :style="{ width: '30rem', 'margin-top': '-300px'}" :draggable="false">
        <div>
            Deseja realmente <b>{{ isFinalizar ? "finalizar" : "cancelar" }}</b> a Atividade <b>{{atividadeEdt.id}} - {{atividadeEdt.titulo}}</b>?
        </div>
        <div class="modal-footer">
            <Button type="button" @click="confirmFinaliza()"><i class="fa fa-check"></i> Confirmar</Button>
            <Button type="button" severity="secondary" @click="showFinalizarAtividade = false"><i class="fa fa-times"></i> Cancelar</Button>
        </div>
    </Dialog>

    <!--Modal de confirmação de finalização/cancelamento de atividades-->
    <Dialog v-model:visible="showIniciarAtividade" modal header="Iniciar Atividade" :style="{ width: '30rem', 'margin-top': '-300px'}" :draggable="false">
        <div>
            Deseja realmente iniciar a Atividade <b>{{atividadeEdt.id ? atividadeEdt.id + " - " + atividadeEdt.titulo : ""}}</b>?<br/>
            <b>Uma vez iniciada, a Atividade não poderá ser cancelada ou deletada.</b>
        </div>
        <div class="modal-footer">
            <Button type="button" @click="confirmIniciar()"><i class="fa fa-check"></i> Confirmar</Button>
            <Button type="button" severity="secondary" @click="showIniciarAtividade = false"><i class="fa fa-times"></i> Cancelar</Button>
        </div>
    </Dialog>
</template>

<script>
import { ref, onMounted, inject, watch } from "vue";
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { useToast } from 'primevue/usetoast';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import { Fieldset, DatePicker } from "primevue";
import { dateObjToBRDate } from "@/assets/DateUtils";
import { renderStatusAtividadeName } from "@/assets/Utils";

export default {
    components: {
        DataTable,
        Column,
        Button,
        Dialog,
        InputText,
        DatePicker,
        Fieldset
    },
    methods: { dateObjToBRDate, renderStatusAtividadeName },
    name: 'TelaAtividades',
    setup(){
        const projeto = inject('projeto');

        watch(projeto, () => {}, { deep: true });

        //Identifica se projeto possui ID, se sim, é edição e realiza operações por request
        //Se não só edita a lista
        const isProjEditar = () =>{
            return !!projeto.value.id;
        }

        const atividadeEdt = ref({});
        const isEditar = ref(true);
        const showEdtModal = ref(false);
        const showDeleteModal = ref(false);
        const showFinalizarAtividade = ref(false);
        const showIniciarAtividade = ref(false);
        //True se forfinalizar, false se for cancelar
        const isFinalizar = ref(false);

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

        let errors = ref({});

        const editAtividade = (atividade) => {
            showEdtModal.value = true;
            isEditar.value = true;
            cleanAtividade();
            if(isProjEditar() && (atividade && atividade.id)){
                fetch("/api/atividade/" + atividade.id)
                    .then((response) => response.json())
                    .then((data) => {
                        if(data.status == "SUCCESS"){
                            const obj = data.dataMap.obj;
                            obj.dataCriacao = dateObjToBRDate(obj.dataCriacao);
                            obj.dataPrevista = dateObjToBRDate(obj.dataPrevista);
                            obj.dataConclusao = dateObjToBRDate(obj.dataConclusao);
                            atividadeEdt.value = obj;
                        } else {
                            toastMsg(data); 
                            showEdtModal.value = false;
                        }
                    });
                return;
            } else if (atividade && atividade.timestamp){
                const obj = getAtividadeFromList(atividade.timestamp);
                if(obj && (obj.id || obj.timestamp)){
                    atividadeEdt.value = obj;
                    return;
                }
            }
            showEdtModal.value = false;
            toastMsg({status: "warn", descricao: "Atividade não encontrada"});
        };

        const isAtividadeEditavel = () =>{
            return atividadeEdt.value.status == 'EM_DESENVOLVIMENTO' || atividadeEdt.value.status == 'NOVO';
        }

        const newAtividade = () => {
            cleanAtividade();
            isEditar.value = false;
            showEdtModal.value = true;
        };

        const cleanAtividade = () =>{
            atividadeEdt.value = {
                id: null,
                titulo: "",
                dataPrevista: "",
                descricao: "",
                status: "NOVO",
                timestamp: null, //Utilizado para identificar atividades ainda não persistidas no lugar do id
            };
            errors.value = {};
        }

        const deleteAtividade = (atividade) => {
            atividadeEdt.value = atividade;
            showDeleteModal.value = true;
        };

        // Função que é chamada quando o usuário confirma a deleção
        const confirmDelete = () => {
            if(isProjEditar() && (atividadeEdt.value && atividadeEdt.value.id)){
                fetch("/api/atividade/" + atividadeEdt.value.id, { method: "DELETE" })
                    .then((response) => response.json())
                    .then((data) => {
                        if(data.status != "ERROR"){
                            removeAtividadeList(atividadeEdt.value.id);
                        }
                        toastMsg(data);
                    })
                    .catch((error) => console.error("Erro ao deletar Atividade: ", error));
                showDeleteModal.value = false;
            } else if (atividadeEdt.value.timestamp){
                removeAtividadeList(atividadeEdt.value.timestamp);
                showDeleteModal.value = false;
                toastMsg({status: "SUCCESS", descricao: "Atividade removida com sucesso"});
            }
        };

        const finalizarAtividade = (atividade, finalCancel) => {
            atividadeEdt.value = atividade;
            showFinalizarAtividade.value = true;
            isFinalizar.value = finalCancel;
        };

        // Função que é chamada quando o usuário confirma a finalização/deleção
        const confirmFinaliza = () => {
            if(isProjEditar() && atividadeEdt.value && atividadeEdt.value.id) {
                fetch((isFinalizar.value ? "/api/atividade/finalizar/" : "/api/atividade/cancelar/") + atividadeEdt.value.id, { method: "PUT" })
                    .then((response) => response.json())
                    .then((data) => {
                        if (data.status == "SUCCESS") {
                            atividadeEdt.value = data.dataMap.obj;
                            addUpdateAtividade(atividadeEdt.value);
                        }
                        toastMsg(data);
                    })
                    .catch((error) => console.error("Erro ao finalizar Atividade: ", error));
                cleanAtividade();
                showFinalizarAtividade.value = false;
                return;
            } else if (atividadeEdt.value && atividadeEdt.value.timestamp){
                atividadeEdt.value.status = (isFinalizar.value ? "FINALIZADO" : "CANCELADO");
                atividadeEdt.value.dataConclusao = new Date();
                addUpdateAtividade(atividadeEdt.value)
            }
        };

        const iniciarAtividade = (atividade) => {
            atividadeEdt.value = atividade;
            showIniciarAtividade.value = true;
        };

        // Função que é chamada quando o usuário confirma a finalização/deleção
        const confirmIniciar = () => {
            if(isProjEditar() && atividadeEdt.value && atividadeEdt.value.id) {
                fetch("/api/atividade/iniciar/" + atividadeEdt.value.id, { method: "PUT" })
                    .then((response) => response.json())
                    .then((data) => {
                        if (data.status == "SUCCESS") {
                            atividadeEdt.value = data.dataMap.obj;
                            addUpdateAtividade(atividadeEdt.value)
                        }
                        toastMsg(data);
                    })
                    .catch((error) => console.error("Erro ao iniciar Atividade: ", error));
                cleanAtividade();
                showIniciarAtividade.value = false;
                return;
            } else if (atividadeEdt.value && atividadeEdt.value.timestamp){
                atividadeEdt.value.status = 'EM_DESENVOLVIMENTO';
                atividadeEdt.value.dataInicio = new Date();
                addUpdateAtividade(atividadeEdt.value);
            }
        };

        const validateForm = () => {
            errors.value = { titulo: "", descricao: "" , dataPrevista: "" };

            if (!atividadeEdt.value.titulo.trim()) {
                errors.value.titulo = "O titulo é obrigatório.";
            }
            if (!atividadeEdt.value.descricao.trim()) {
                errors.value.descricao = "A descrição é obrigatória.";
            }
            if (!atividadeEdt.value.dataPrevista) {
                errors.value.dataPrevista = "Informe a Data Prevista.";
            } else {
                const currentDate = new Date();
                currentDate.setHours(0,0,0,0);
                if(typeof atividadeEdt.value.dataPrevista == "string"){
                    var dividir = atividadeEdt.value.dataPrevista.split("/");
                    var conquistar = new Date(dividir[2], dividir[1], dividir[0]);
                    atividadeEdt.value.dataPrevista = conquistar;
                }
                if (atividadeEdt.value.dataPrevista.getTime() < currentDate.getTime()) {
                    errors.value.dataPrevista = "A Data não pode ser anterior à data atual.";
                }
            }

            return !errors.value.titulo && !errors.value.descricao && !errors.value.dataPrevista;
        };

        const saveAtividade = () => {
            if (!validateForm()) return;
            if(isProjEditar()){
                atividadeEdt.value.projetoParent = {id: projeto.value.id};
                atividadeEdt.value.dataCriacao = "";
                
                fetch("/api/atividade", {
                    method: isEditar.value ? "PUT" : "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(atividadeEdt.value),
                })
                .then((response) => response.json())
                .then((data) => {
                    if(data.status == "SUCCESS"){
                        showEdtModal.value = false;
                        atividadeEdt.value = data.dataMap.obj;
                        addUpdateAtividade(atividadeEdt.value)
                    }
                    toastMsg(data);
                })
                .catch(error => console.error("Erro ao salvar Atividade: ", error));
            } 
            //Se não for uma edição de projeto, não tem como persistir atividade, logo deve usar um substituto como identificador
            else {
                atividadeEdt.value.dataCriacao = atividadeEdt.value.dataCriacao ? atividadeEdt.value.dataCriacao : new Date();
                atividadeEdt.value.timestamp = atividadeEdt.value.timestamp ? atividadeEdt.value.timestamp : new Date().getTime();
                addUpdateAtividade(atividadeEdt.value);
                showEdtModal.value = false;
                toastMsg({status: "SUCCESS", descricao: "Atividade salva com sucesso"});
            }
        };

        /**
         * Busca atividade da lista por identificador ou timestamp
         * @param atividadeId identificador ou timestamp da atividade
         */
        const getAtividadeFromList = (atividadeId) =>{
            if(!projeto.value.atividadeList) return;
            let atvReturn = {};
            projeto.value.atividadeList.some(a =>{
                if(a.id == atividadeId || a.timestamp == atividadeId){
                    atvReturn = a;
                    return true;
                }
                return false;
            });
            return atvReturn;
        }

        /**
         * Atualiza/insere nova atividade na lista
         * @param atividade atividade a ser inserida/atualizada
         */
        const addUpdateAtividade = (atividade) =>{
            if(!projeto.value.atividadeList) {
                projeto.value.atividadeList = [atividade]
                return;
            }
            let index = -1;
            const found = projeto.value.atividadeList.some((a, i) =>{
                if((a.id && atividade.id && (a.id == atividade.id)) || 
                    (a.timestamp && atividade.timestamp && (a.timestamp == atividade.timestamp))){
                    index = i;
                    return true;
                }
                return false;
            });
            if(found && index >= 0){
                projeto.value.atividadeList[index] = atividade;
            } else {
                projeto.value.atividadeList.push(atividade);
            }
        }

        /**
         * Remove atividade da lista pelo seu identificador ou timestamp
         * @param atividadeId identificador ou timestamp da atividade
         */
        const removeAtividadeList = (atividadeId) =>{
            if(!projeto.value.atividadeList) return;
            let index = -1;
            const found = projeto.value.atividadeList.some((a, i) =>{
                if(a.id == atividadeId || (a.timestamp && a.timestamp == atividadeId)){
                    index = i;
                    return true;
                }
                return false;
            });
            if(found && index >= 0){
                projeto.value.atividadeList.splice(index, 1);
            }
        }

        onMounted(() => {
        });

        return { 
            //Variáveis
            isEditar, showEdtModal, showDeleteModal, atividadeEdt, errors, 
            showFinalizarAtividade, showIniciarAtividade, isFinalizar, projeto,
            //Métodos
            finalizarAtividade, confirmFinaliza, editAtividade, deleteAtividade, newAtividade, 
            saveAtividade, confirmIniciar, iniciarAtividade, confirmDelete, isAtividadeEditavel 
        };
    }
}
</script>

<style lang="scss">

    .mainDiv {
        padding: 0 25px;
        border-radius: 10px;
        height: 40vh;
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

    .pr-0{
        padding-right: 0 !important;
    }

    .pl-0{
        padding-left: 0 !important;
    }

    .miniDiv{
        margin-top: 10px;
    }

</style>