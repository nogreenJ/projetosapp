const dateObjToBRDate = (date) =>{
    if(!date){
        return "";
    }
    return new Date(date).toLocaleDateString();
}

module.exports = {
    dateObjToBRDate: dateObjToBRDate,
};