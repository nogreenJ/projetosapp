<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <router-link class="navbar-brand nav-link" to="/cliente">MS Projetos</router-link>

        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navBarLinks"
            aria-controls="navBarLinks" aria-expanded="false" aria-label="Mostrar abas">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div id="navBarLinks" class="collapse navbar-collapse navBarLinks">
            <div class="navbar-nav mr-auto">
              <router-link to="/cliente" class="nav-item nav-link">Clientes</router-link>
            </div>
            <div class="navbar-nav mr-auto">
              <router-link to="/projeto" class="nav-item nav-link">Projetos</router-link>
            </div>
        </div>
    </nav> 
  </div>
  <div class="bgApp">
    <router-view/>
  </div>
  <Toast position="bottom-right"/>
</template>

<script>
import Toast from 'primevue/toast';
export default {
  name: 'App',
  components: {
    Toast
  }
}
</script>

<style lang="scss">
    @import './assets/styles.scss';
</style>