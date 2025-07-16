<template>
  <v-container>
    <v-snackbar
      v-model="snackbar.status"
      :timeout="snackbar.timeout"
      :color="snackbar.color"
    >
      {{ snackbar.text }}
      <v-btn style="margin-left: 80px;" text @click="snackbar.status = false">
        Close
      </v-btn>
    </v-snackbar>

    <!-- 업로드 이미지 미리보기 -->
    <v-card class="pa-4 mb-6">
      <v-file-input
        v-model="uploadedImage"
        label="이미지 업로드 (Variations 용)"
        accept="image/*"
        show-size
        clearable
        :multiple="false"
        @change="onFileChange"
      ></v-file-input>

      <div v-if="previewUrl" class="mt-5">
        <div>업로드 이미지 미리보기:</div>
        <v-img :src="previewUrl" max-width="1000" max-height="1500" contain></v-img>
      </div>

      <v-btn class="mt-5"
        color="primary"
        :disabled="!uploadedImage || loading"
        @click="generateVariation"
      >
        이미지 변형 (Variations) 생성
      </v-btn>

      <v-progress-circular
        v-if="loading"
        indeterminate
        color="primary"
        class="ml-4"
      ></v-progress-circular>

      <div v-if="variationUrls.length" class="mt-6">
        <div>변형된 이미지:</div>
        <v-row>
          <v-col
            v-for="(url, idx) in variationUrls"
            :key="idx"
            cols="12"
            sm="6"
            md="4"
          >
            <v-img :src="url" max-width="1000" max-height="1500" contain></v-img>
            <v-btn
              color="secondary"
              small
              class="mt-2"
              @click="downloadImage(url)"
            >
              다운로드
            </v-btn>
          </v-col>
        </v-row>
      </div>
    </v-card>

    <!-- 텍스트 프롬프트로 이미지 생성 -->
    <v-card class="pa-4 mb-6">
      <v-text-field
        v-model="prompt"
        label="이미지 생성 텍스트 프롬프트"
        clearable
      ></v-text-field>
    
      <v-select class="mt-5"
        v-model="model"
        :items="models"
        label="AI 이미지 생성 모델 선택"
      ></v-select>

      <v-btn class="mt-5"
        color="success"
        :disabled="!prompt || loading"
        @click="generateImage"
      >
        텍스트로 이미지 생성
      </v-btn>

      <v-progress-circular
        v-if="loading"
        indeterminate
        color="success"
        class="ml-4"
      ></v-progress-circular>

      <div v-if="generatedUrls.length" class="mt-6">
        <div>생성된 이미지:</div>
        <v-row>
          <v-col
            v-for="(url, idx) in generatedUrls"
            :key="idx"
            cols="12"
            sm="6"
            md="4"
          >
            <v-img :src="url" max-width="1000" max-height="1792" contain></v-img>
            <v-btn
              color="secondary"
              small
              class="mt-2"
              @click="downloadImage(url)"
            >
              다운로드
            </v-btn>
          </v-col>
        </v-row>
      </div>
    </v-card>

    <!-- 기존 테이블 등 기존 UI는 그대로 유지 -->

  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: 'aiGrid',
  data() {
    return {
      uploadedImage: null,
      previewUrl: null,
      variationUrls: [],
      generatedUrls: [],
      prompt: '',
      model: 'dall-e-3',
      models: ['dall-e-2', 'dall-e-3'],
      loading: false,
      snackbar: {
        status: false,
        timeout: 3000,
        color: 'error',
        text: '',
      },
    };
  },
  methods: {
    onFileChange(file) {
      if (!file) {
        this.previewUrl = null;
        return;
      }
      const fileObj = Array.isArray(file) ? file[0] : file;
      if (!(fileObj instanceof File)) {
        this.previewUrl = null;
        return;
      }
      this.previewUrl = URL.createObjectURL(fileObj);
      this.variationUrls = [];
    },
    async uploadGeneratedToAzureReplicate() {
      if (!this.generatedUrls.length) return;
      try {
        for (const url of this.generatedUrls) {
          await axios.post('/aicreate/upload-to-azure-replicate', { imageUrl: url });
        }
        this.snackbar.status = true;
        this.snackbar.color = 'success';
        this.snackbar.text = '생성된 이미지 Azure 업로드 완료!';
      } catch (error) {
        this.snackbar.status = true;
        this.snackbar.color = 'error';
        this.snackbar.text = '생성 이미지 업로드 실패: ' + error.message;
      }
    },
    downloadImage(url) {
      // 이미지 URL로 a 태그 생성 후 클릭해서 다운로드 처리
      const a = document.createElement('a');
      a.href = url;
      // URL에서 파일명 추출, 없으면 기본명
      const urlParts = url.split('/');
      const filename = urlParts[urlParts.length - 1].split('?')[0] || 'download.png';
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    },
  },
};
</script>

<style scoped>
/* 필요시 스타일 조정 */
</style>
