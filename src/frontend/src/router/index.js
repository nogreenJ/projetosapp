import { createRouter, createWebHistory } from 'vue-router'
import TelaClientes from '../views/TelaClientes.vue'
import TelaHome from '../views/TelaHome.vue';
import TelaProjetos from '../views/TelaProjetos.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: TelaHome
  },
  {
    path: '/cliente',
    name: 'Clientes',
    component: TelaClientes
  },
  {
    path: '/projeto',
    name: 'Projetos',
    component: TelaProjetos
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router