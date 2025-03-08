<!--
    COMPONENTE PARA SELEÇÃO DE CLIENTES CADASTRADOS NO SISTEMA
-->
<template>
    <div class="card flex justify-center">
        <Select 
            v-model="selectedClienteId" 
            filter 
            :options="clienteLista" 
            optionLabel="nome" 
            optionValue="id" 
            @change="updateCliente"
            :disabled="disabled"
            placeholder="Selecione um Cliente" 
            checkmark 
            :highlightOnSelect="false" 
            class="w-full md:w-56" 
            emptyFilterMessage="Nenhum Cliente encontrado"/>
    </div>
</template>

<script setup>
    import { ref, defineModel, onMounted, defineProps, watch } from "vue";
    import { useToast } from 'primevue/usetoast';
    import Select from 'primevue/select';

    defineProps({ disabled: Boolean });

    const model = defineModel();

    let clienteLista = ref([]);
    const selectedClienteId = ref(null);

    const toast = useToast();

    const toastMsg = (params) => {
        params.status = params.status.toLowerCase();
        toast.add({
            severity: params.status == "warning" ? 'warn' : params.status,
            summary: params.descricao,
            life: 5000
        });
    };

    const loadList = () => {
        fetch("/api/cliente")
            .then((response) => response.json()) .then((data) => {
                if(data.status == "SUCCESS"){
                    clienteLista.value = data.dataMap.list;
                } else {
                    toastMsg(data);
                }
            })
            .catch(error => console.error("Erro ao buscar Clientes: ", error));
    };

    const updateCliente = () => {
        model.value = selectedClienteId.value;
    };

    watch(model, (newValue) => {
        selectedClienteId.value = newValue;
    });

    onMounted(() => {
        loadList();
    });
</script>
