<script setup>
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'
</script>

<template>
    <ul>
        <VerticalNavSectionTitle :item="{ heading: '이미지생성 테스트' }" />
        <VerticalNavLink
            :item="{
                title: 'Ai',
                to: '/ais',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '이미지 검사' }" />
        <VerticalNavLink
            :item="{
                title: '검사결과',
                to: '/results',
            }"
        />
    </ul>
</template>
