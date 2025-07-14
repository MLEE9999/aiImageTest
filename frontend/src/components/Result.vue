<template>
    <div>
        <Number
            label="ResultId"
            v-model="value.resultId"
            :editMode="editMode"
        />
        <String
            label="ImageId"
            v-model="value.imageId"
            :editMode="editMode"
        />
        <String
            label="Status"
            v-model="value.status"
            :editMode="editMode"
        />
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'

export default {
    name: 'Result',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "results",
        value: {
        }
    }),
    created(){
    },
    computed:{
    },
    methods: {
    },
}
</script>
