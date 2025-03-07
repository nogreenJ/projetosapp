import { createRouter, createWebHistory } from 'vue-router'
import TelaClientes from '../views/TelaClientes.vue'
import TelaHome from '../views/TelaHome.vue';

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
  /*{
    path: '/careers',
    name: 'Careers',
    component:Careers
  },*/
]

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router