<template>

    <!--Listagem de Clientes do sistema-->
    <div class="mainDiv">
        <div class="row header">
            <div class="col-md-4 offset-md-4">
                <h2 class="title">
                    Clientes 
                </h2>
            </div>
            <div class="actionsTopBar col-md-4">
                <Button   
                    data-target="#modalCliente" data-toggle="modal"
                    @click="newCliente()">
                    <i class="fa fa-plus"></i>
                </Button>
            </div>
        </div>
        <DataTable :value="clienteLista" tableStyle="" paginator :rows="12" sortField="id" :sortOrder="-1">
            <Column field="id" header="Código" style="width: 10%"></Column>
            <Column field="nome" header="Nome" style="width: 35%"></Column>
            <Column field="email" header="E-mail" style="width: 24%">
                <template #body="slotProps">
                    {{ slotProps.data.email ? slotProps.data.email : '-' }}
                </template>
            </Column>
            <Column field="telefone" header="Telefone" style="width: 24%">
                <template #body="slotProps">
                    {{ slotProps.data.telefone ? slotProps.data.telefone : '-' }}
                </template>
            </Column>
            <Column header="Ações"  style="width: 7%">
                <template #body="slotProps">
                    <div>
                        <Button  
                            variant="text"
                            @click="editCliente(slotProps.data)">
                            <i class="fa fa-pencil"></i>
                        </Button>
                        <Button  
                            variant="text"
                            @click="deleteCliente(slotProps.data)">
                            <i class="fa fa-trash"></i>
                        </Button>
                    </div>
                </template>
            </Column>
        </DataTable>
    </div>

    <!--Modal de criação e edição de clientes-->
    <Dialog v-model:visible="showEdtModal" modal header="Cliente" :style="{ width: '25rem', 'margin-top': '-300px'}" :draggable="false">
        <form @submit.prevent="saveCliente">
            <div class="row">
                <div class="col-md-3">
                    <label for="codigo">Código</label>
                </div>
                <div class="col-md-5">
                    <InputText type="text" id="codigo" v-model="clienteEdt.id" class="form-control" placeholder="Código" readonly/>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <label for="nome">Nome</label>
                </div>
                <div class="col-md-9">
                    <InputText type="text" id="nome" v-model="clienteEdt.nome" class="form-control" placeholder="Nome" :invalid="!!errors.nome" maxlength="40"/>
                    <span v-if="errors.nome" class="errormsg">{{ errors.nome }}</span>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <label for="email">E-mail</label>
                </div>
                <div class="col-md-6">
                    <InputText type="text" id="email" v-model="clienteEdt.email" class="form-control" placeholder="E-mail" :invalid="!!errors.email" maxlength="30"/>
                    <span v-if="errors.email" class="errormsg">{{ errors.email }}</span>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <label for="telefone">Telefone</label>
                </div>
                <div class="col-md-6">
                    <InputText type="text" id="telefone" v-model="clienteEdt.telefone" class="form-control" placeholder="Telefone" :invalid="!!errors.telefone" maxlength="15"/>
                    <span v-if="errors.telefone" class="errormsg">{{ errors.telefone }}</span>
                </div>
            </div>
            <div class="modal-footer">
                <Button type="submit" label="Salvar"><i class="fa fa-check"></i> Salvar</Button>
                <Button type="button" label="Cancelar" severity="secondary" @click="showEdtModal = false"><i class="fa fa-times"></i>Cancelar</Button>
            </div>
        </form>
    </Dialog>

    <!--Modal de confirmação de deleção de clientes-->
    <Dialog v-model:visible="showDeleteModal" modal header="Excluir Cliente" :style="{ width: '30rem', 'margin-top': '-300px'}" :draggable="false">
        <div>
            Deseja realmente excluir o Cliente <b>{{clienteEdt.id}} - {{clienteEdt.nome}}</b>?
        </div>
        <div class="modal-footer">
            <Button type="button" @click="confirmDelete()"><i class="fa fa-check"></i> Confirmar</Button>
            <Button type="button" severity="secondary" @click="showDeleteModal = false"><i class="fa fa-times"></i> Cancelar</Button>
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

export default {
    components: {
        DataTable,
        Column,
        Button,
        Dialog,
        InputText
    },
    name: 'TelaClientes',
    setup(){
        let clienteLista = ref([]);
        let clienteEdt = ref({});
        let isEditar = ref(true);
        let showEdtModal = ref(false);
        let showDeleteModal = ref(false);
        const toast = useToast();

        let errors = ref({
            nome: "",
            email: "",
            telefone: "",
        });

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

        const editCliente = (cliente) => {
            showEdtModal.value = true;
            isEditar.value = true;
            cleanCliente();
            if(cliente && cliente.id){
                fetch("/api/cliente/" + cliente.id)
                    .then((response) => response.json())
                    .then((data) => {
                        if(data.status == "SUCCESS"){
                            clienteEdt.value = data.dataMap.obj;
                        } else {
                            toastMsg(data); 
                            showEdtModal.value = false;
                        }
                    });
            }
        };

        const newCliente = () => {
            cleanCliente();
            isEditar.value = false;
            showEdtModal.value = true;
        };

        const cleanCliente = () =>{
            clienteEdt.value = {
                id: null,
                nome: "",
                email: "",
                telefone: "",
            };
            errors.value = { nome: "", email: "", telefone: "" };
        }

        const deleteCliente = (cliente) => {
            clienteEdt.value = cliente;
            showDeleteModal.value = true;
        };

        // Função que é chamada quando o usuário confirma a deleção
        const confirmDelete = () => {
            if (clienteEdt.value && clienteEdt.value.id) {
                fetch("/api/cliente/" + clienteEdt.value.id, { method: "DELETE" })
                    .then((response) => response.json())
                    .then((data) => {
                        if (data.status == "SUCCESS") {
                            loadList();
                        }
                        toastMsg(data);
                    })
                    .catch((error) => console.error("Erro ao deletar Cliente: ", error));
                cleanCliente();
                showDeleteModal.value = false;
            }
        };

        const validateForm = () => {
            errors.value = { nome: "", email: "", telefone: "" };

            if (!clienteEdt.value.nome.trim()) {
                errors.value.nome = "O nome é obrigatório.";
            }

            if (clienteEdt.value.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(clienteEdt.value.email)) {
                errors.value.email = "E-mail inválido.";
            }

            if (clienteEdt.value.telefone && !/^\(?\d{2}\)?\s?\d{4,5}-?\d{4}$/.test(clienteEdt.value.telefone)) {
                errors.value.telefone = "Telefone inválido.";
            }

            return !errors.value.nome && !errors.value.email && !errors.value.telefone;
        };

        const saveCliente = () => {
            if (!validateForm()) return;

            fetch("/api/cliente", {
                method: isEditar.value ? "PUT" : "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(clienteEdt.value),
            })
            .then((response) => response.json())
            .then((data) => {
                if(data.status == "SUCCESS"){
                    showEdtModal.value = false;
                    loadList();
                }
                toastMsg(data);
            })
            .catch(error => console.error("Erro ao salvar Cliente: ", error));
        };

        const loadList = () => {
            fetch("/api/cliente")
                .then((response) => response.json())
                .then((data) => {
                    if(data.status == "SUCCESS"){
                        clienteLista.value = data.dataMap.list;
                    } else {
                        toastMsg(data);
                    }
                })
                .catch(error => console.error("Erro ao listar Clientes: ", error));
        }

        onMounted(() => {
            loadList();
        });

        return { clienteLista, isEditar, showEdtModal, showDeleteModal, clienteEdt, errors,    
            editCliente, deleteCliente, newCliente, saveCliente, loadList, confirmDelete };
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

    .modal-footer {
        margin-top: 20px;
    }

    .errormsg {
        color: red;
        font-size: 13px;
    }

</style>