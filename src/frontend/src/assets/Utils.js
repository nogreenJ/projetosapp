
/**
 * Tratamento para visualização de status dos projetos
 * @param status status do projeto, em string 
 * @returns status em string normalizada
 */
const renderStatusProjetoName = (status) =>{
    switch(status){
        case "EM_DESENVOLVIMENTO":
            return "Em Desenvolvimento";
        case "FINALIZADO":
            return "Finalizado";
        case "CANCELADO":
            return "Cancelado";
        default:
            return "-";
    }
}

/**
 * Tratamento para visualização de status das atividades
 * @param status status da atividade, em string 
 * @returns status em string normalizada
 */
const renderStatusAtividadeName = (status) =>{
    switch(status){
        case "NOVO":
            return "Nova";
        case "EM_DESENVOLVIMENTO":
            return "Em Desenvolvimento";
        case "FINALIZADO":
            return "Finalizada";
        case "CANCELADO":
            return "Cancelada";
        default:
            return "-";
    }
}

module.exports = {
    renderStatusProjetoName: renderStatusProjetoName,
    renderStatusAtividadeName: renderStatusAtividadeName,
};